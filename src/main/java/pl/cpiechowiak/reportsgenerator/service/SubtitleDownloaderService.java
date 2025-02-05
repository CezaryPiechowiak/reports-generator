package pl.cpiechowiak.reportsgenerator.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import pl.cpiechowiak.reportsgenerator.shared.model.ReportResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SubtitleDownloaderService {

    private final ChatModel chatModel;

    public SubtitleDownloaderService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public ReportResponse getChatResponse(String videoId) throws IOException, InterruptedException {
        Runtime.getRuntime().exec(String.format(
                "docker run --rm -v \"%s:/downloads\" jauderho/yt-dlp " +
                        "--write-auto-sub --sub-lang pl " +
                        "--skip-download " +
                        "-o \"/downloads/subtitles.vtt\" " +
                        "https://www.youtube.com/watch?v=%s", new File("."), videoId
        )).waitFor();

        FileSystemResource subtitles = new FileSystemResource("subtitles.vtt.pl.vtt");
        String subtitlesContent = StreamUtils.copyToString(subtitles.getInputStream(), StandardCharsets.UTF_8);

        ChatResponse summary = chatModel.call(
                new Prompt("Streść materiał wideo na podstawie napisów: " + subtitlesContent,
                        OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_3_5_TURBO).build()));


        ChatResponse homework = chatModel.call(
                new Prompt("Opracuj konkretne zadanie domowe na podstawie dostarczonej treści, a następnie dodaj praktyczne wskazówki, które pomogą odbiorcy skutecznie je zrealizować: " + summary,
                        OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_3_5_TURBO).build()));

        return new ReportResponse(summary.getResult().getOutput().getContent(), homework.getResult().getOutput().getContent());
    }
}