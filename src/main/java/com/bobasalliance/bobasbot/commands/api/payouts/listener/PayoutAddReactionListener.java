//package com.bobasalliance.bobasbot.commands.api.payouts.listener;
//
//import java.sql.Time;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Optional;
//
//import org.javacord.api.event.message.reaction.ReactionAddEvent;
//
//import com.bobasalliance.bobasbot.commands.shared.components.beans.EventDetails;
//import com.bobasalliance.bobasbot.database.dao.PayoutTimeDao;
//import com.bobasalliance.bobasbot.database.repository.PayoutTimeRepository;
//import com.vdurmont.emoji.Emoji;
//
//public class PayoutAddReactionListener extends AbstractReactionListener {
//    private final GregorianCalendar payoutCalendar;
//    private final PayoutTimeRepository payoutTimeRepository;
//    private final EventDetails eventDetails;
//    private final String swgohggLink;
//    private final String flag;
//
//    public PayoutAddReactionListener(final PayoutTimeRepository payoutTimeRepository, final GregorianCalendar payoutCalendar,
//                                     final String swgohggLink, final String flag, final Emoji emojiOK, final Emoji emojiCancel, final EventDetails eventDetails) {
//        super(emojiOK, emojiCancel, eventDetails.getUser());
//        this.payoutCalendar = payoutCalendar;
//        this.payoutTimeRepository = payoutTimeRepository;
//        this.eventDetails = eventDetails;
//        this.swgohggLink = swgohggLink;
//        this.flag = flag;
//    }
//
//    @Override
//    protected Optional<String> doReaction(final ReactionAddEvent event) {
//        long timeMillisUTC = (long) payoutCalendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
//        timeMillisUTC = timeMillisUTC + (long) payoutCalendar.get(Calendar.MINUTE) * 60 * 1000;
//        timeMillisUTC = timeMillisUTC - (long) payoutCalendar.getTimeZone().getOffset(System.currentTimeMillis());
////        payoutTimeRepository.save(createPayoutTimeDao(timeMillisUTC));
//        payoutTimeRepository.savePayoutTime(eventDetails.getChannelId(), eventDetails.getArgument("user_name"), new Time(timeMillisUTC), flag, swgohggLink);
//        return Optional.empty();
//    }
//
//    private PayoutTimeDao createPayoutTimeDao(final long timeMillisUTC) {
//        PayoutTimeDao payoutTimeDao = new PayoutTimeDao();
//        payoutTimeDao.setPayoutTime(new Time(timeMillisUTC));
//        payoutTimeDao.setFlag(flag);
//        payoutTimeDao.setChannelId(eventDetails.getChannelId());
//        payoutTimeDao.setSwgohggLink(swgohggLink);
//        payoutTimeDao.setUserName(eventDetails.getArgument("user_name"));
//        return payoutTimeDao;
//    }
//}
