package com.unimelb.family_artifact_register.PresentationLayer.EventPreseneter;

import com.unimelb.family_artifact_register.FoundationLayer.EventModel.Event;
import com.unimelb.family_artifact_register.FoundationLayer.EventModel.EventListener;
import com.unimelb.family_artifact_register.FoundationLayer.EventModel.EventManager;
import com.unimelb.family_artifact_register.FoundationLayer.UserModel.UserInfoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventViewModel {
    public static final String TAG = EventViewModel.class.getSimpleName();
    private static EventViewModel eventViewModel;
    private List<String> attendEvent;
    private List<EventListener> fragments;

    private EventViewModel() {
        attendEvent = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    public static EventViewModel getInstance() {
        if (eventViewModel == null) {
            eventViewModel = new EventViewModel();
            return eventViewModel;
        }
        return eventViewModel;
    }

    public List<Event> getRecommendedEvent() {
        return EventManager.getInstance().getEventByUid(UserInfoManager.getInstance().getCurrentUid())
                .stream()
                .filter(x -> !attendEvent.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Event> getAttendedEvent() {
        return EventManager.getInstance().getEventByUid(UserInfoManager.getInstance().getCurrentUid())
                .stream()
                .filter(x -> attendEvent.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addAttendEvent(String eventId) {
        attendEvent.add(eventId);
        fragments.forEach(EventListener::notifyEventsChange);
    }

    public void cancelAttendEvent(String eventId) {
        attendEvent.remove(eventId);
        fragments.forEach(EventListener::notifyEventsChange);
    }

    public void addObserver(EventListener fragment) {
        fragments.add(fragment);
    }
}
