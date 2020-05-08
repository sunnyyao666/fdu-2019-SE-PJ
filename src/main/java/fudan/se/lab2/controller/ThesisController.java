package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.AuditApplicationRequest;
import fudan.se.lab2.controller.request.AuditThesisRequest;
import fudan.se.lab2.service.ThesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author YHT
 */
@RestController
public class ThesisController {
    private ThesisService thesisService;

    @Autowired
    public ThesisController(ThesisService thesisService) {
        this.thesisService = thesisService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitThesis(Long id, String conferenceFullName, String title, String summary, String authors, String topics, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(thesisService.submitThesis(id, conferenceFullName, title, summary, authors, topics, file));
    }

    @PostMapping("/startAudit")
    public ResponseEntity<?> startAudit(@RequestBody AuditApplicationRequest request) {
        if (request.isPassed())
            return ResponseEntity.ok(thesisService.startAudit1(request.getConferenceFullName()));
        else return ResponseEntity.ok(thesisService.startAudit2(request.getConferenceFullName()));
    }

    @PostMapping("/pcGetTheses")
    public ResponseEntity<?> pcGetTheses(String conferenceFullName) {
        return ResponseEntity.ok(thesisService.pcGetTheses(conferenceFullName));
    }

    @PostMapping("/auditThesis")
    public ResponseEntity<?> auditThesis(@RequestBody AuditThesisRequest auditThesisRequest) {
        return ResponseEntity.ok(thesisService.auditThesis(auditThesisRequest));
    }

    @PostMapping("/endAudit")
    public ResponseEntity<?> endAudit(String conferenceFullName) {
        return ResponseEntity.ok(thesisService.endAudit(conferenceFullName));
    }
}