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

import java.lang.Comparable;

/** Class that defines nodes for the start and end times of Events */
public final class EventTime implements Comparable<EventTime> {
  private final Event event;
  private final boolean isAStart;
  private final int time;

  public EventTime(int timeValue, Event eventValue, boolean isAStartValue) {
    this.event = eventValue;
    this.isAStart = isAStartValue;
    this.time = timeValue;
  }

  public Event getEvent() {
    return event;
  }

  public int getTime() {
    return time;
  }

  public boolean isAStart() {
    return isAStart;
  }

  public int compareTo(EventTime other) {
    if (this.time != other.time) {
      return this.time - other.time;
    }
    return isAStart ? -1 : 1;
  }
}
