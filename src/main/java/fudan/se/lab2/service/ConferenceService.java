package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApplyConferenceRequest;
import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.exception.ConferenceNameDuplicatedException;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author YHT
 */
@Service
public class ConferenceService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private ConferenceRepository conferenceRepository;

    @Autowired
    public ConferenceService(UserRepository userRepository, AuthorityRepository authorityRepository, ConferenceRepository conferenceRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.conferenceRepository = conferenceRepository;
    }

    public Conference applyConference(ApplyConferenceRequest request) throws BadCredentialsException, ConferenceNameDuplicatedException {
        String fullName = request.getFullName();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        Conference conference = conferenceRepository.findByFullName(request.getFullName());
        if (conference != null) throw new ConferenceNameDuplicatedException(request.getFullName());
        conference = new Conference(request.getAbbreviation(), fullName, request.getPlace(), request.getStartDate(), request.getEndDate(), request.getDeadline(), request.getReleaseTime(), request.getTopic(), userRepository.findByUsername(userDetails.getUsername()));
        conferenceRepository.save(conference);
        return conference;
    }

    public Set<Conference> listConferences(String text) {
        if ("admin".equals(text)) return conferenceRepository.findAllByApplying(true);
        else if ("submission".equals(text)) return conferenceRepository.findAllByValid(true);
        return null;
    }

    public boolean auditConferenceApplication(String conferenceFullName, boolean passed) {
        Conference conference = conferenceRepository.findByFullName(conferenceFullName);
        if (conference == null) return false;
        conference.setApplying(false);
        if (passed) {
            User creator = conference.getCreator();
            conference.setValid(true);
            authorityRepository.save(new Authority("Chair", creator, conference.getFullName(), null));
        }
        conferenceRepository.save(conference);
        return true;
    }

    public Conference searchConference(String conferenceFullName) {
        return conferenceRepository.findByFullName(conferenceFullName);
    }


    public boolean changeSubmissionState(String conferenceFullName, boolean passed) {
        Conference conference = conferenceRepository.findByFullName(conferenceFullName);
        conference.setSubmitting(passed);
        conferenceRepository.save(conference);
        return true;
    }

    public boolean startAudit(String conferenceFullName, boolean passed) {
        Conference conference = conferenceRepository.findByFullName(conferenceFullName);
        if (authorityRepository.findAllByAuthorityAndConferenceFullName("PC Member", conferenceFullName).size() < 2)
            return false;
        conference.setAuditing(true);
        conference.setSubmitting(false);
        conferenceRepository.save(conference);
        return true;
    }
}
