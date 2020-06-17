// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public final class FindMeetingQuery {
  private static final Event ENTIRE_DAY_EVENT = new Event("Entire Day Event", TimeRange.WHOLE_DAY, Arrays.asList());

  /** Returns a Collection of TimeRanges that satisfy the details of the MeetingRequest based on the events */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> requiredAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();

    // Initialize PriorityQueue with start and end 'EventTime' nodes
    PriorityQueue<EventTime> eventTimes = new PriorityQueue<>();
    for (Event event : events) {
      TimeRange eventTimeRange = event.getWhen();
      eventTimes.add(new EventTime(eventTimeRange.start(), event, true));
      eventTimes.add(new EventTime(eventTimeRange.end(), event, false));
    }
    // Add EventTime nodes to the beginning and end of the day, to handle edge cases
    eventTimes.add(new EventTime(ENTIRE_DAY_EVENT.getWhen().start(), ENTIRE_DAY_EVENT, true));
    eventTimes.add(new EventTime(ENTIRE_DAY_EVENT.getWhen().end(), ENTIRE_DAY_EVENT, false));

    // Keep track of potential meetings and the Attendees that are 'busy' while going through the PriorityQueue
    Set<Event> ongoingEvents = new HashSet<Event>();
    List<TimeRange> possibleMeetings = new ArrayList<>();
    List<TimeRange> possibleMeetingsWithOptAttendees = new ArrayList<>();

    // 'Traverse' throughout the whole day, one EventTime at a time,
    // and find potential blocks of time for Meetings
    EventTime previousEventTime = eventTimes.poll();
    ongoingEvents.add(previousEventTime.getEvent());
    while (!eventTimes.isEmpty()) {
      EventTime currentEventTime = eventTimes.poll();

      // Check if any ongoing event conflicts with the optional or required attendees
      boolean meetingConflictsReqAttendees = false;
      boolean meetingConflictsOptAttendees = false;
      for (Event event : ongoingEvents) {
        for (String attendee : event.getAttendees()) {
          if (requiredAttendees.contains(attendee)) {
            meetingConflictsReqAttendees = true;
            meetingConflictsOptAttendees = true;
            break;
          }
          if (optionalAttendees.contains(attendee)) {
            meetingConflictsOptAttendees = true;
          }
        }
        if (meetingConflictsReqAttendees) break;
      }

      // Only add to the lists of possible TimeRanges if all required attendees are present,
      // and all optional attendees if necessary. Make sure the TimeRange created is valid.
      if (previousEventTime.getTime() != currentEventTime.getTime()) {
        TimeRange meetingToAdd = TimeRange.fromStartEnd(previousEventTime.getTime(), currentEventTime.getTime(), false);
        if (!meetingConflictsReqAttendees) {
          possibleMeetings.add(meetingToAdd);
        }
        if (!meetingConflictsOptAttendees) {
          possibleMeetingsWithOptAttendees.add(meetingToAdd);
        }
      }

      // Update the current events that are in 'progress' as I traverse through the day, as well as the PreviousEventTime
      if (currentEventTime.isAStart()) {
        ongoingEvents.add(currentEventTime.getEvent());
      }
      else {
        ongoingEvents.remove(currentEventTime.getEvent());
      }
      previousEventTime = currentEventTime;
    }

    getFinalizedTimeRanges(possibleMeetingsWithOptAttendees, request.getDuration());
    getFinalizedTimeRanges(possibleMeetings, request.getDuration());

    // Check if meetings were found where all optional attendees could attend, return
    if (possibleMeetingsWithOptAttendees.isEmpty()) {
      // Handle specific case where no possible meetings have a Required Attendee
      if (requiredAttendees.isEmpty() && !possibleMeetings.isEmpty()
          && possibleMeetings.get(0).start() == 0//TimeRange.WHOLE_DAY.start()
          && possibleMeetings.get(0).end() == 1440) {//TimeRange.WHOLE_DAY.end()) {
        return Arrays.asList();
      }
      return possibleMeetings;
    }

    return possibleMeetingsWithOptAttendees;
  }

  /** Method that merges adjacent TimeRange objects, and filters out objs that don't meet the required duration */
  private void getFinalizedTimeRanges(List<TimeRange> timeRanges, long requiredDuration) {
    // If there's only one TimeRange, just check if it meets the required duration
    if (timeRanges.size() == 1 && timeRanges.get(0).duration() < requiredDuration) {
      timeRanges.remove(0);
    }
    else if (timeRanges.size() > 1) {
      for (int i = 1; i < timeRanges.size(); i++) {
        TimeRange currentTimeRange = timeRanges.get(i);
        TimeRange previousTimeRange = timeRanges.get(i - 1);
        // Merge Adjacent TimeRanges
        if (currentTimeRange.start() == previousTimeRange.end()) {
          timeRanges.remove(i);
          timeRanges.set(i - 1, TimeRange.fromStartEnd(previousTimeRange.start(), currentTimeRange.end(), false));
          // Handle index offset caused by removal
          i--;
        }
        // No longer need to merge TimeRanges, just make sure the last added TimeRange is 'long' enough
        else if (timeRanges.get(timeRanges.size() - 1).duration() < requiredDuration) {
          timeRanges.remove(timeRanges.size() - 1);
        }
      }
    }
  }
}
