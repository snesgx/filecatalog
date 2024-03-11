package com.kudori.FileIndexer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
        
        private CompletableFuture<String> indexingProcess = null;
        String lastIndexingResult = "";
        
	@GetMapping("/indexing")
	public String indexing(@RequestParam Optional<String> path) {
            return ie.startIndexing(path.get());
            /*
                String getResult = "Nothing happened";
            
                if (indexingProcess == null) {
                    if (path.isPresent()) {
                        indexingProcess = CompletableFuture.supplyAsync(() -> lastIndexingResult = ie.startIndexing(path.get()));
                        indexingProcess.thenAccept(result -> indexingProcess = null);
                        getResult = "Process started";
                    } else {
                        getResult = "No indexing in progress" + (lastIndexingResult.isEmpty() ? "" : ", last result was: " + lastIndexingResult);
                    }
                } else {
                    if (indexingProcess.isDone()) {
                        try {
                            getResult = "Process finished correctly: " + indexingProcess.get();  // Will throw an exception
                            indexingProcess = null;
                        } catch (Exception e) {
                            getResult = "The task failed with an exception: " + e.getMessage();
                        }
                        
                        indexingProcess = null;
                        
                    } else  getResult = "Process still in progress, " + String.valueOf(ie.getItemsCount());
                }
               
                return getResult;
*/
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
        
