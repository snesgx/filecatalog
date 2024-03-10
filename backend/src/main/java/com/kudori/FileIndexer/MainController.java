package com.kudori.FileIndexer;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MainController {

        @Autowired
        IndexingEngine ie;
        
        @Autowired
        FileIndexerSearch fis;
        
        
	@GetMapping("/indexing")
	public int indexing(@RequestParam String path) {
                return ie.StartIndexing(path);
	}

	@GetMapping("/getsummary")
	public List<Map<String,Object>> getSummary() {
                return fis.getSummary();
	}

        @GetMapping("/")
        public RedirectView redirectToHTML() {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/index.html");
            return redirectView;
        }
  
        
}
        
