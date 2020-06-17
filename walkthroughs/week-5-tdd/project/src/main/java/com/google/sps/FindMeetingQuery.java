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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {
  /** Returns a Collection of TimeRanges that satisfy the details of the MeetingRequest based on the events */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Get the invalid TimeRanges that don't have the required participants
    List<TimeRange> invalidTimeRanges = getInvalidTimeRanges(events, request.getAttendees());
    // Sort and merge the invalid TimeRanges, in order to easily distinguish the valid TimeRanges
    Collections.sort(invalidTimeRanges, TimeRange.ORDER_BY_START);
    invalidTimeRanges = mergeInvalidTimeRanges(invalidTimeRanges);
    // Get TimeRanges that aren't invalid - these are the possible meeting times
    Collection<TimeRange> meetingTimeRanges = getMeetingTimeRanges(invalidTimeRanges, request);
    return meetingTimeRanges;
  }

  /** Method that goes through the collection of events for TimeRanges that are 'invalid' meeting times */
  private List<TimeRange> getInvalidTimeRanges(Collection<Event> events, Collection<String> requiredAttendees) {
    List<TimeRange> invalidTimeRanges = new ArrayList<>();
    for (Event event : events) {
      if (doesEventHaveARequiredAttendee(event, requiredAttendees)) {
        invalidTimeRanges.add(event.getWhen());
      }
    }
    return invalidTimeRanges;
  }

  /** Method that returns true if an event has a required attendee attending, false otherwise */
  private boolean doesEventHaveARequiredAttendee(Event event, Collection<String> requiredAttendees) {
    Set<String> busyAttendees = event.getAttendees();
    for (String busyAttendee : busyAttendees) {
      if (requiredAttendees.contains(busyAttendee)) {
        return true;
      }
    }
    return false;
  }

  /** Method that merges the invalid TimeRanges that overlap, and returns a List of these new time ranges */
  private List<TimeRange> mergeInvalidTimeRanges(List<TimeRange> invalidTimeRanges) {
    List<TimeRange> mergedInvalidTimeRanges = new ArrayList<>();
    // Perform merge operation only if there is more than 1 invalid TimeRange
    if (invalidTimeRanges.size() == 1) {
      mergedInvalidTimeRanges.add(invalidTimeRanges.get(0));
    }
    else if (invalidTimeRanges.size() > 1) {
      // Variables to track the start and end of the new TimeRange to create, that merges two or more TimeRanges
      int startTime = invalidTimeRanges.get(0).start();
      int endTime = invalidTimeRanges.get(0).end();
      for (int i = 1; i < invalidTimeRanges.size(); i++) {
        TimeRange currentTimeRange = invalidTimeRanges.get(i);
        TimeRange previousTimeRange = invalidTimeRanges.get(i - 1);
        if (currentTimeRange.overlaps(previousTimeRange)) {
          // Update TimeRange start and end if two of the TimeRanges overlap
          startTime = Math.min(startTime, Math.min(currentTimeRange.start(), previousTimeRange.start()));
          endTime = Math.max(endTime, Math.max(currentTimeRange.end(), previousTimeRange.end()));
        }
        else {
          // Add the merged TimeRange to List, and re-start cycle of creating a merged TimeRange
          mergedInvalidTimeRanges.add(TimeRange.fromStartEnd(startTime, endTime, false));
          startTime = currentTimeRange.start();
          endTime = currentTimeRange.end();
        }
      }
      // One merged TimeRange isn't yet added to the List
      mergedInvalidTimeRanges.add(TimeRange.fromStartEnd(startTime, endTime, false));
    }

    return mergedInvalidTimeRanges;
  }

  /** Method to get all TimeRanges that are not part of the merged invalid TimeRanges, and meet the required duration */
  private Collection<TimeRange> getMeetingTimeRanges(List<TimeRange> invalidTimeRanges, MeetingRequest request) {
    Collection<TimeRange> meetingTimeRanges = new ArrayList<>();
    // Add a temporary TimeRange at the end, in order to get the last valid meeting time
    invalidTimeRanges.add(TimeRange.fromStartDuration(TimeRange.END_OF_DAY + 1, 0));
    int startTime = 0;
    for (int i = 0; i < invalidTimeRanges.size(); i++) {
      TimeRange currentInvalidTimeRange = invalidTimeRanges.get(i);
      // Meetings range from the end of the previous invalid TimeRange to the start of the current invalidTimeRange
      TimeRange possibleMeeting = TimeRange.fromStartEnd(startTime, currentInvalidTimeRange.start(), false);
      if (possibleMeeting.duration() >= request.getDuration()) {
        meetingTimeRanges.add(possibleMeeting);
      }
      // Set the new Meeting startTime to the end of the previous invalid TimeRange
      startTime = currentInvalidTimeRange.end();
    }
    return meetingTimeRanges;
  }

  private void debugList(Collection<TimeRange> list, String type) {
    System.out.println(type.toUpperCase() + " LIST");
    for (TimeRange t : list) {
      System.out.println(t);
    }
    System.out.println();
  }
}
