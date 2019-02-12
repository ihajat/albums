package com.example.owner.myalbumsapplication.api;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.owner.myalbumsapplication.dto.CommentDto;
import com.example.owner.myalbumsapplication.utils.adapters.LiveDataCallAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(JUnit4.class)
public class AlbumApiTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private AlbumApi service;

    private MockWebServer mockWebServer;

    @Before
    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(AlbumApi.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    /*
        Test 1 -> Test the rest api point using the mock server
     */
    @Test
    public void testAlbumsRestPointSuccessful() throws IOException, InterruptedException {
        enqueueResponse("data.json");
        List<CommentDto> commentDtoList = LiveDataTestUtil.getValue(service.browseCommentsLiveData(1,100)).body;

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/albums?page=1&per_page=100")); //https://jsonplaceholder.typicode.com
    }

    /*
        Test 2 -> Test the Albums Size is Correct
    */
    @Test
    public void testAlbumsSizeSuccessful() throws IOException, InterruptedException {
        enqueueResponse("data.json");
        List<CommentDto> commentDtoList = LiveDataTestUtil.getValue(service.browseCommentsLiveData(1,100)).body;

        assertThat(commentDtoList.size(), is(100));
    }

    /*
        Test 3 -> Test Gets Firsts Album Successfully
    */
    @Test
    public void testGetFirstAlbumSuccessful() throws IOException, InterruptedException {
        enqueueResponse("data.json");
        List<CommentDto> commentDtoList = LiveDataTestUtil.getValue(service.browseCommentsLiveData(1,100)).body;

        CommentDto commentDto = commentDtoList.get(0);
        assertThat(commentDto.getTitle(), is("quidem molestiae enim"));
    }

    private void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.emptyMap());
    }

    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse
                .setBody(source.readString(StandardCharsets.UTF_8)));
    }
}