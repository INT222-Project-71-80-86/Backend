package int222.project.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import int222.project.files.FileInfo;
import int222.project.services.FileStoreServices;

@RequestMapping(path = "/api/file")
@Controller
public class FileController {
	
	@Autowired FileStoreServices file;
	
	// Get All File 
	@GetMapping("")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = this.file.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	// Get Image File
	@GetMapping(value = "/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = this.file.load(filename); // Get Resource File
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file); // Return Resource as IMAGE File
	}
	

}
