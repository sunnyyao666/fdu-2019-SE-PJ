package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YHT
 */
@RestController
public class ConferenceController {
    private ConferenceService conferenceService;

    @Autowired
    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyConference(@RequestBody ApplyConferenceRequest request) {
        return ResponseEntity.ok(conferenceService.applyConference(request));
    }

    @PostMapping("/listConferences")
    public ResponseEntity<?> listConferences(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(conferenceService.listConferences(request.getText()));
    }

    @PostMapping("/auditConferenceApplication")
    public ResponseEntity<?> auditConferenceApplication(@RequestBody AuditApplicationRequest request) {
        return ResponseEntity.ok(conferenceService.auditConferenceApplication(request.getConferenceFullName(), request.isPassed()));
    }

    @PostMapping("/searchConference")
    public ResponseEntity<?> searchConference(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(conferenceService.searchConference(request.getConferenceFullName()));
    }

    @PostMapping("/searchUsers")
    public ResponseEntity<?> searchUsers(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(conferenceService.searchUsers(request.getText(), request.getConferenceFullName()));
    }

    @PostMapping("/invitePCMember")
    public ResponseEntity<?> invitePCMember(@RequestBody InvitePCMemberRequest request) {
        String[] invitee = request.getInvitee();
        for (String username : invitee) conferenceService.invitePCMember(username, request.getConferenceFullName());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/listInviteHistory")
    public ResponseEntity<?> listInviteHistory(@RequestBody AuditApplicationRequest request) {
        return ResponseEntity.ok(conferenceService.listInviteHistory(request.getConferenceFullName()));
    }

    @PostMapping("/auditPCInvitationApplication")
    public ResponseEntity<?> auditPCInvitationApplication(@RequestBody AuditApplicationRequest request) {
        return ResponseEntity.ok(conferenceService.auditPCInvitationApplication(request.getConferenceFullName(), request.isPassed()));
    }

    @PostMapping("/changeSubmissionState")
    public ResponseEntity<?> changeSubmissionState(@RequestBody AuditApplicationRequest request) {
        return ResponseEntity.ok(conferenceService.changeSubmissionState(request.getConferenceFullName(), request.isPassed()));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitThesis(String conferenceFullName, String title, String summary, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(conferenceService.submitThesis(conferenceFullName, title, summary, file));
    }

}
