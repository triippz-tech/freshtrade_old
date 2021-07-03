package com.triippztech.freshtrade.service.mail;

import com.triippztech.freshtrade.domain.Item;
import com.triippztech.freshtrade.domain.Reservation;
import com.triippztech.freshtrade.domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

@Service
public class BuyerMailService {

    private final Logger log = LoggerFactory.getLogger(BuyerMailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public BuyerMailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendBuyerConfirmReservationEmail(Reservation reservation, User buyer, Item foundItem, Integer quantity) throws IOException {
        log.debug("Sending Buyer Reservation Confirmation Email To: {}", buyer.getEmail());

        Locale locale = Locale.forLanguageTag(buyer.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, buyer);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("itemName", foundItem.getName());
        context.setVariable("quantity", quantity);
        context.setVariable("imageResourceName", "logo_freshtrade.png");
        context.setVariable("reservationNumber", reservation.getReservationNumber());
        context.setVariable("seller", foundItem.getOwner().getLogin());
        context.setVariable("event", foundItem.getTradeEvent().getEventName());
        context.setVariable(
            "pickupDate",
            foundItem.getTradeEvent().getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z"))
        );
        String content = templateEngine.process("mail/buyerReserveConfirmEmail", context);
        String subject = messageSource.getMessage("email.buyer.reserve.confirm.title", null, locale);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        InputStream imageIs = null;
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setTo(buyer.getEmail());
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setReplyTo("noreply@freshtrade.io");
            message.setText(content, true);

            imageIs = this.getClass().getClassLoader().getResourceAsStream("images/logo_freshtrade.png");
            assert imageIs != null;
            byte[] imageByteArray = IOUtils.toByteArray(imageIs);

            final InputStreamSource imageSource = new ByteArrayResource(imageByteArray);
            message.addInline("logo_freshtrade.png", imageSource, "image/png");

            log.debug(message.toString());
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", buyer.getEmail());
        } catch (MailException | MessagingException | IOException e) {
            log.warn("Email could not be sent to user '{}'", buyer.getEmail(), e);
        } finally {
            if (imageIs != null) imageIs.close();
        }
    }
}
