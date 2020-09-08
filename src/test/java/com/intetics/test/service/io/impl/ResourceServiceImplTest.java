package com.intetics.test.service.io.impl;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.base.TestableOperation;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 17:04
 *
 * @author d.velichkevich
 */
public class ResourceServiceImplTest extends AbstractBaseMockTestCase {

    private static final String TAG_CREATE_MESSAGES = "createMessages";
    private static final String TAG_CREATE_PROPERTIES = "createProperties";

    private static final String TEST_FILENAME_MESSAGES = "testMessages.properties";
    private static final String TEST_FILENAME_PROPERTIES = "testApplication.properties";


    @Mock
    private Properties messagesMock;

    @Mock
    private Properties propertiesMock;

    private ResourceServiceImpl resourceService;

    private InOrder order;


    @BeforeEach
    public void setUp(TestInfo testInfo) throws Exception{
        setUp();

        if (testInfo.getTags().contains(TAG_CREATE_MESSAGES)) {
            //noinspection ResultOfMethodCallIgnored
            new File(TEST_FILENAME_MESSAGES).createNewFile();
            try (FileWriter writer = new FileWriter(TEST_FILENAME_MESSAGES)) {
                writer.write("test.key=Test MESSAGE");
            }
        }

        if (testInfo.getTags().contains(TAG_CREATE_PROPERTIES)) {
            //noinspection ResultOfMethodCallIgnored
            new File(TEST_FILENAME_PROPERTIES).createNewFile();
        }

        resourceService = new ResourceServiceImpl(TEST_FILENAME_MESSAGES, TEST_FILENAME_PROPERTIES);
        setMocks();

        resourceService = spy(resourceService);

        order = inOrder(resourceService, messagesMock, propertiesMock);
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) throws Exception {
        if (testInfo.getTags().contains(TAG_CREATE_MESSAGES)) {
            //noinspection ResultOfMethodCallIgnored
            new File(TEST_FILENAME_MESSAGES).delete();
        }

        if (testInfo.getTags().contains(TAG_CREATE_PROPERTIES)) {
            //noinspection ResultOfMethodCallIgnored
            new File(TEST_FILENAME_PROPERTIES).delete();
        }
    }

    private void setMocks() {
        resourceService.setMessages(messagesMock);
        resourceService.setProperties(propertiesMock);
    }

    @Test
    public void testConstruction() throws Exception {
        // Given - When
        resourceService = new ResourceServiceImpl() {

            @Override
            void initialize() {
                addActions("ResourceServiceImpl -> initialize");
            }
        };

        // Then
        assertThat(resourceService.getMessages()).isEqualTo(new Properties());
        assertThat(resourceService.getProperties()).isEqualTo(new Properties());
        assertThat(resourceService.getMessagesFilename()).isEqualTo(ResourceServiceImpl.MESSAGES_FILENAME);
        assertThat(resourceService.getPropertiesFilename()).isEqualTo(ResourceServiceImpl.PROPERTIES_FILENAME);

        checkActions("ResourceServiceImpl -> initialize");
    }

    @Test
    @Tag(value = TAG_CREATE_PROPERTIES)
    public void testInitialization() throws Exception {
        // Given
        mockLoadingFromClasspath(messagesMock, TEST_FILENAME_MESSAGES);
        doNothing().when(resourceService).loadFileResource(propertiesMock, TEST_FILENAME_PROPERTIES);

        // Given
        resourceService.initialize();

        // Then
        verifyLoadingFromClasspath(order, messagesMock, TEST_FILENAME_MESSAGES);
        order.verify(resourceService).loadFileResource(propertiesMock, TEST_FILENAME_PROPERTIES);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testInitializationWithoutFile() throws Exception {
        // Given
        mockLoadingFromClasspath(messagesMock, TEST_FILENAME_MESSAGES);
        mockLoadingFromClasspath(propertiesMock, TEST_FILENAME_PROPERTIES);

        // When
        resourceService.initialize();

        // Then
        verifyLoadingFromClasspath(order, messagesMock, TEST_FILENAME_MESSAGES);
        verifyLoadingFromClasspath(order, propertiesMock, TEST_FILENAME_PROPERTIES);

        order.verifyNoMoreInteractions();
    }

    private void mockLoadingFromClasspath(Properties mock, String filename) {
        doNothing().when(resourceService).loadClasspathResource(mock, filename);
    }

    private void verifyLoadingFromClasspath(InOrder order, Properties mock, String filename) {
        order.verify(resourceService).loadClasspathResource(mock, filename);
    }

    @Test
    @Tag(value = TAG_CREATE_MESSAGES)
    public void testLoadingClasspathResource() throws Exception {
        // Given - When - Then
        checkLoadingResource(this::mockGettingClasspathResourceInputStream, this::performClasspathResourceLoading, this::verifyGettingClasspathResourceInputStream);
    }

    @Test
    public void testLoadingClasspathResourceWithoutFile() throws Exception {
        // Given - When - Then
        checkLoadingResourceWithoutFile(
                this::mockGettingClasspathResourceInputStreamNull, this::performClasspathResourceLoading, this::verifyGettingClasspathResourceInputStreamNull);
    }

    private void verifyGettingClasspathResourceInputStreamNull() throws Exception{
        verifyGettingClasspathResourceInputStream();
        order.verify(messagesMock).load(nullable(InputStream.class));

    }

    private void performClasspathResourceLoading() {
        resourceService.loadClasspathResource(messagesMock, TEST_FILENAME_MESSAGES);
    }

    @Test
    @Tag(value = TAG_CREATE_MESSAGES)
    public void testLoadingFileResource() throws Exception {
        // Given - When - Then
        checkLoadingResource(() -> {}, this::loadMessagesFileResource, () -> {});
    }

    private void checkLoadingResource(TestableOperation additionalMocks, TestableOperation operation, TestableOperation additionalVerifications) throws Exception {
        // Given
        additionalMocks.execute();
        doNothing().when(messagesMock).load(any(InputStream.class));

        // When
        operation.execute();

        // Then
        additionalVerifications.execute();
        order.verify(messagesMock).load(any(InputStream.class));
        order.verifyNoMoreInteractions();
    }

    private void mockGettingClasspathResourceInputStream() {
        doReturn(mock(InputStream.class)).when(resourceService).getClasspathResourceInputStream(TEST_FILENAME_MESSAGES);
    }

    private void verifyGettingClasspathResourceInputStream() {
        order.verify(resourceService).getClasspathResourceInputStream(TEST_FILENAME_MESSAGES);
    }

    @Test
    public void testLoadingFileResourceWithoutFile() throws Exception {
        // Given - When - Then
        checkLoadingResourceWithoutFile(() -> {}, this::loadMessagesFileResource, () -> {});
    }

    private void loadMessagesFileResource() {
        resourceService.loadFileResource(messagesMock, TEST_FILENAME_MESSAGES);
    }

    private void checkLoadingResourceWithoutFile(
            TestableOperation additionalMocks, final TestableOperation operation, TestableOperation additionalVerifications) throws Exception {

        // Given
        additionalMocks.execute();
        doNothing().when(resourceService).displayFatalError(TEST_FILENAME_MESSAGES);

        // When - Then
        assertThatThrownBy(operation::execute).isInstanceOf(RuntimeException.class).getCause().isInstanceOf(IOException.class);

        additionalVerifications.execute();
        order.verify(resourceService).displayFatalError(TEST_FILENAME_MESSAGES);
        order.verifyNoMoreInteractions();
    }

    private void mockGettingClasspathResourceInputStreamNull() throws Exception {
        doReturn(null).when(resourceService).getClasspathResourceInputStream(TEST_FILENAME_MESSAGES);
        doThrow(new IOException()).when(messagesMock).load(nullable(InputStream.class));
    }

    @org.junit.Test
    public void testDisplayingFatalError() throws Exception {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doReturn(new PrintStream(outputStream)).when(resourceService).getConsoleOutputStream();

        // When
        resourceService.displayFatalError(TEST_FILENAME_MESSAGES);

        // Then
        order.verify(resourceService).getConsoleOutputStream();
        order.verifyNoMoreInteractions();

        assertThat(outputStream.toString()).isEqualTo("!!! " + ResourceServiceImpl.MESSAGE_PATTERN_RESOURCE_NOT_FOUND + " '" + TEST_FILENAME_MESSAGES + "'");
    }

    @Test
    public void testGettingMessage() throws Exception {
        // Given
        doReturn("TEST_VALUE").when(resourceService).getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, null);

        // When
        String actual = resourceService.getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING);

        // Then
        order.verify(resourceService).getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, null);
        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo("TEST_VALUE");
    }

    @Test
    public void testGettingProperty() throws Exception {
        // Given - When - Then
        checkGettingPropertyValue(propertiesMock, this::invokeGettingProperty, propertiesMock);
    }

    private String invokeGettingProperty() {
        return resourceService.getProperty(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, "DEFAULT_VALUE");
    }

    @Test
    public void testGettingMessageWithDefaultValue() throws Exception {
        // Given - When - Then
        checkGettingPropertyValue(messagesMock, this::invokeGettingMessage, messagesMock);
    }

    private String invokeGettingMessage() {
        return resourceService.getMessage(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, "DEFAULT_VALUE");
    }

    private void checkGettingPropertyValue(Properties mock, Supplier<String> operation, Properties expectedMock) {
        // Given
        doReturn("TEST_VALUE").when(mock).getProperty(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, "DEFAULT_VALUE");

        // When
        String actual = operation.get();

        // Then
        order.verify(expectedMock).getProperty(ConsoleServiceImpl.MESSAGE_KEY_ERROR_IN_STRING, "DEFAULT_VALUE");
        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo("TEST_VALUE");
    }
}
