package com.tradingjournal_pro.backend.controller;


import com.tradingjournal_pro.backend.dto.JournalRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.service.JournalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/journals")
public class JournalController {

    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping("/{userId}/add-journal")
    public ResponseBody addJournal(@Valid @RequestBody JournalRequestBody journalRequestBody,
                                   @PathVariable String userId) {
        return journalService.addJournal(journalRequestBody, userId);
    }

    @GetMapping("/{userId}/all-journals")
    public ResponseBody getAllJournals(@PathVariable String userId) {
        return  journalService.getAllJournals(userId);
    }

    @GetMapping("{userId}/{journalId}")
    public  ResponseBody getJournal(@PathVariable String userId,  @PathVariable String journalId) {
        return journalService.getJournal(userId, journalId);
    }

    @PutMapping("{userId}/{journalId}")
    public ResponseBody updateJournal(@Valid @RequestBody JournalRequestBody journalRequestBody,
                                      @PathVariable String userId,
                                      @PathVariable String journalId) {
        return journalService.updateJournal(journalRequestBody, userId, journalId);
    }

    @DeleteMapping("{userId}/{journalId}")
    public ResponseBody squareOffJournal(@PathVariable String userId,  @PathVariable String journalId){
        return journalService.squareOffJournal(userId, journalId);
    }
}
