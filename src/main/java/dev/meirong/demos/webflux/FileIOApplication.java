package dev.meirong.demos.webflux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class FileIOApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileIOApplication.class, args);
    }

    @RestController
    @RequestMapping("/files")
    public class FileController {
        @Autowired
        private FileIOService fileIOService;

        // curl -X POST -F "file=@data.txt" http://localhost:8080/files/upload
        @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public Mono<Void> uploadFile(@RequestPart("file") FilePart filePart) {
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("File is required"));
            }
            return fileIOService.uploadFile(filePart);
        }

        // curl http://localhost:8080/files/download/data.txt
        @GetMapping(value = "/download/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        public Flux<DataBuffer> downloadFile(@PathVariable String filename) {
            return fileIOService.downloadFile(filename);
        }
    }

    @Service
    public class FileIOService {
        private static final String UPLOAD_DIR = "uploads";

        public Mono<Void> uploadFile(FilePart filePart) {
            Path filePath = Path.of(UPLOAD_DIR, filePart.filename());
            // 如果要进一步优化文件上传的性能，或许需要更细粒度的控制，可以使用`DataBufferUtils`提供的工具来手动处理文件上传。
            //  它可以更好地控制缓冲区大小和I/O操作，以提高性能。
            if (!Files.exists(filePath.getParent())) {
                try {
                    Files.createDirectories(filePath.getParent());
                } catch (IOException e) {
                    return Mono.error(e);
                }
            }
            return filePart.transferTo(filePath)
                    .then(Mono.empty());
        }

        // public Mono<Void> uploadFile(FilePart filePart) {
        //     Path filePath = Path.of(UPLOAD_DIR, filePart.filename());
        //     return DataBufferUtils.write(filePart.content(), filePath, StandardOpenOption.CREATE)
        //             .then();
        // }


        public Flux<DataBuffer> downloadFile(String filename) {
            Path filePath = Path.of(UPLOAD_DIR, filename);

            return DataBufferUtils.readInputStream(
                    () -> Files.newInputStream(filePath, StandardOpenOption.READ),
                    new DefaultDataBufferFactory(), // Use DefaultDataBufferFactory
                    1024 // Buffer size
            );
        }

    }
}
