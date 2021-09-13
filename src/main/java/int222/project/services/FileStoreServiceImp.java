package int222.project.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStoreServiceImp implements FileStoreServices {
	
	private final Path root;
	
	 public FileStoreServiceImp(@Value("${project.upload-path}")String path) {
		root = Paths.get(path);
	}

	@Override
	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (Exception e) {
			throw new RuntimeException("Could not create directory for upload.");
		}

	}

	@Override
	public String save(MultipartFile file) {
		try {
			String filename = generateRandomString(file);
			Files.copy(file.getInputStream(), this.root.resolve(filename));
//			System.out.println("Succesfully save file with filename: "+filename); // For Testing
			return filename;
		} catch (Exception e) {
			throw new RuntimeException("Could not store file. Error: "+e.getMessage());
		}
	}
	
	@Override
	public String generateRandomString(MultipartFile file) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,12);
		return uuid+"-"+file.getOriginalFilename();
	}

	@Override
	public Resource load(String filename) {
		try {
		      Path file = root.resolve(filename);
		      Resource resource = new UrlResource(file.toUri());

		      if (resource.exists() || resource.isReadable()) {
		        return resource;
		      } else {
		        throw new RuntimeException("Could not read the file!");
		      }
		    } catch (MalformedURLException e) {
		      throw new RuntimeException("Error: " + e.getMessage());
		    }
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}
	
	@Override
	public void deleteOne(String filename) {
		try {
			FileSystemUtils.deleteRecursively(root.resolve(filename));
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
		      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		    } catch (IOException e) {
		      throw new RuntimeException("Could not load the files!");
		    }
	}

}
