    package com.blue.bluearchive.config;

    import com.blue.bluearchive.member.dto.CustomUserDetails;
    import org.springframework.data.domain.AuditorAware;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;

    import java.util.Optional;

    public class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
    //        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication auth = context.getAuthentication();
            Object principal = auth.getPrincipal();
            String userId = "";

            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                userId = Long.toString(userDetails.getIdx());
            } else if (principal instanceof String) {
                userId = (String) principal;
            }

            return Optional.of(userId);
        }
    }
