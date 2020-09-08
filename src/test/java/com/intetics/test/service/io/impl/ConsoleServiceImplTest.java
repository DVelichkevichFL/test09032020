package com.intetics.test.service.io.impl;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.base.TestableOperation;
import com.intetics.test.model.StringGroup;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.ResourceService;
import com.intetics.test.service.io.impl.aggregator.ConsoleInputAggregator;
import com.intetics.test.base.aggregator.EmptyStringAggregator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 1:43
 *
 * @author d.velichkevich
 */
public class ConsoleServiceImplTest extends AbstractBaseMockTestCase {

    private static final String TEST_ANOTHER_SOURCE_STRING = "test(test1(test2(test3),test4,test5),test6)";


    private ConsoleServiceImpl consoleService;

    private InOrder order;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        consoleService = spy(new ConsoleServiceImpl(getResourceServiceMock()));

        order = inOrder(consoleService, getResourceServiceMock());
    }

    @Test
    public void testPrintingLine() throws Exception {
        // Given
        doNothing().when(consoleService).printlnImpl(ConsoleServiceImpl.PATTERN_OUTPUT, ConsoleService.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");

        // When
        consoleService.println(ConsoleService.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");

        // Then
        order.verify(consoleService).printlnImpl(ConsoleServiceImpl.PATTERN_OUTPUT, ConsoleService.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");
        order.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        TEST_ANOTHER_SOURCE_STRING + ":" + TEST_ANOTHER_SOURCE_STRING,
        "\n:" + TEST_SOURCE_STRING
    }, delimiter = ':')
    public void testRequestingString(
            @AggregateWith(ConsoleInputAggregator.class) String input, @AggregateWith(ConsoleInputAggregator.class) String expected) throws Exception {

        // Given
        ResourceService resourceServiceMock = getResourceServiceMock();
        when(resourceServiceMock.getMessage(ConsoleServiceImpl.MESSAGE_KEY_DEFAULT_STRING)).thenReturn(TEST_SOURCE_STRING);
        doNothing().when(consoleService).print(ConsoleServiceImpl.PATTERN_INPUT, ConsoleServiceImpl.MESSAGE_KEY_ENTER_STRING, TEST_SOURCE_STRING, TEST_DEFAULT_STRATEGY);
        when(consoleService.getConsoleInputStream()).thenReturn(new ByteArrayInputStream(input.getBytes()));

        // When
        String actual = consoleService.requestString(TEST_DEFAULT_STRATEGY);

        // Then
        order.verify(resourceServiceMock).getMessage(ConsoleServiceImpl.MESSAGE_KEY_DEFAULT_STRING);
        order.verify(consoleService).print(ConsoleServiceImpl.PATTERN_INPUT, ConsoleServiceImpl.MESSAGE_KEY_ENTER_STRING, TEST_SOURCE_STRING, TEST_DEFAULT_STRATEGY);
        order.verify(consoleService).getConsoleInputStream();
        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testPrinting() throws Exception {
        // Given - When - Then
        checkPrintableOperation(this::invokePrinting, ">>> Test message #1: TEST");
    }

    private void invokePrinting() {
        consoleService.print(ConsoleServiceImpl.PATTERN_OUTPUT, ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");
    }

    @Test
    public void testGeneratingOutput() throws Exception{
        // Given
        Stack<StringGroup> stack = createGeneratingOutputStack();
        doReturn(stack).when(consoleService).addAllGroups(new Stack<>(), stack.peek().getParentGroup().getChildrenGroups());
        stack = mockGroupProcessing(stack);
        mockGroupProcessing(stack);

        // When
        consoleService.generateOutput(createTestHierarchy());

        // Then
        Stack<StringGroup> expectedStack = createGeneratingOutputStack();
        order.verify(consoleService).addAllGroups(new Stack<>(), expectedStack.peek().getParentGroup().getChildrenGroups());

        expectedStack = verifyGroupProcessing(expectedStack);
        verifyGroupProcessing(expectedStack);

        order.verifyNoMoreInteractions();
    }

    private Stack<StringGroup> mockGroupProcessing(Stack<StringGroup> stack) {
        Stack<StringGroup> result = popGroup(stack);
        doReturn(result).when(consoleService).processGroup(stack);
        return result;
    }

    private Stack<StringGroup> verifyGroupProcessing(Stack<StringGroup> stack) {
        Stack<StringGroup> result = popGroup(stack);
        order.verify(consoleService).processGroup(stack);
        return result;
    }

    @Test
    public void testProcessingGroup() throws Exception {
        // Given
        doReturn("- ").when(consoleService).generatePrefix(1);
        doNothing().when(consoleService).printlnImpl(ConsoleServiceImpl.PATTERN_CLEAN_OUTPUT, ConsoleServiceImpl.MESSAGE_KEY_RESULT, "- ", "TOKEN2");

        Stack<StringGroup> stack = popGroup(createGeneratingOutputStack());
        doReturn(stack).when(consoleService).addAllGroups(stack, new ArrayList<>());

        // When
        Stack<StringGroup> actual = consoleService.processGroup(createGeneratingOutputStack());

        // Then
        order.verify(consoleService).generatePrefix(1);
        order.verify(consoleService).printlnImpl(ConsoleServiceImpl.PATTERN_CLEAN_OUTPUT, ConsoleServiceImpl.MESSAGE_KEY_RESULT, "- ", "TOKEN2");

        Stack<StringGroup> expectedStack = popGroup(createGeneratingOutputStack());
        order.verify(consoleService).addAllGroups(expectedStack, new ArrayList<>());

        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo(expectedStack);
    }

    private Stack<StringGroup> createGeneratingOutputStack() {
        StringGroup root = createTestHierarchy();
        Stack<StringGroup> result = new Stack<>();
        result.addAll(0, root.getChildrenGroups());
        return result;
    }

    private StringGroup createTestHierarchy() {
        StringGroup result = new StringGroup();
        result.getChildrenGroups().add(new StringGroup(1, "TOKEN1", result));
        result.getChildrenGroups().add(new StringGroup(1, "TOKEN2", result));
        return result;
    }

    private Stack<StringGroup> popGroup(Stack<StringGroup> stack) {
        Stack<StringGroup> result = new Stack<>();
        result.addAll(0, stack);
        result.pop();
        return result;
    }

    @Test
    public void testAddingAllGroups() throws Exception {
        //Given - When
        Stack<StringGroup> actual = consoleService.addAllGroups(new Stack<>(), createTestHierarchy().getChildrenGroups());

        // Then
        Stack<StringGroup> expectedStack = new Stack<>();
        StringGroup expectedRoot = new StringGroup();
        expectedStack.push(new StringGroup(1, "TOKEN2", expectedRoot));
        expectedStack.push(new StringGroup(1, "TOKEN1", expectedRoot));
        assertThat(actual).isEqualTo(expectedStack);
    }

    @Test
    public void testPrintingLineImpl() throws Exception {
        // Given - When - Then
        checkPrintableOperation(this::invokePrintingLineImpl, (">>> Test message #1: TEST" + System.lineSeparator()));
    }

    private void checkPrintableOperation(TestableOperation operation, String expectedOutput) throws Exception{
        // Given
        doReturn("Test message #1: TEST").when(consoleService).getOutputMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doReturn(new PrintStream(outputStream)).when(consoleService).getConsoleOutputStream();

        // When
        operation.execute();

        // Then
        order.verify(consoleService).getOutputMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");
        order.verify(consoleService).getConsoleOutputStream();
        order.verifyNoMoreInteractions();

        assertThat(outputStream.toString()).isEqualTo(expectedOutput);
    }

    private void invokePrintingLineImpl() {
        consoleService.printlnImpl(ConsoleServiceImpl.PATTERN_OUTPUT, ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");
    }

    @Test
    public void testGettingOutputMessage() throws Exception {
        // Given
        ResourceService resourceServiceMock = getResourceServiceMock();
        doReturn(">>> Test message #%s: %s").when(resourceServiceMock).getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING);

        // When
        String actual = consoleService.getOutputMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, 1, "TEST");

        // Then
        order.verify(resourceServiceMock).getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING);
        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo(">>> Test message #1: TEST");
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0,",
        "1,- ",
        "2,-- "
    })
    public void testGeneratingPrefix(int prefixLength, @AggregateWith(EmptyStringAggregator.class) String expectedPrefix) throws Exception {
        // Given - When
        String actual = consoleService.generatePrefix(prefixLength);

        // Then
        assertThat(actual).isEqualTo(expectedPrefix);
    }
}
