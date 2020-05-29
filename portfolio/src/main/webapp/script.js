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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greetingContainer');
  greetingContainer.innerText = greeting;
}

const images = [
  {name: 'basketball.jpg', label: 'Playing basketball with the Tennis Intramural Team!'},
  {name: 'chess.jpg', label: 'Playing chess with my roommate!'},
  {name: 'crochet.jpg', label: 'Crocheting simple beanies!'},
  {name: 'smoothie.jpg', label: 'Making delicious smoothies!'},
  {name: 'statefarm.jpg', label: 'State Farm Internship!'},
  {name: 'tennis.jpg', label: 'Tennis, with the Club Team at UT Austin!'},
  {name: 'UIL_state.jpg', label: 'Competitive Programming!'},
  {name: 'Uke.jpg', label: 'Playing the Ukulele!'}
]

const DIRECTION_UP = 'up';
const DIRECTION_DOWN = 'down';
const RANDOM_SLIDE = 'random';

let slideShowIndex = 0;

/* Navigate to the slide at slideShowIndex - 1 */
function previousSlide() {
  updateSlideShowIndex(DIRECTION_DOWN);
  let image = images[slideShowIndex];
  displayNewSlide(image);
}

/* Navigate to the slide at slideShowIndex + 1 */
function nextSlide() {
  updateSlideShowIndex(DIRECTION_UP);
  let image = images[slideShowIndex];
  displayNewSlide(image);
}

/* Navigate to a random slide */
function randomSlide() {
  updateSlideShowIndex(RANDOM_SLIDE);
  let image = images[slideShowIndex];
  displayNewSlide(image);
}

/* Display slideshow, with correctly navigated content */
function displayNewSlide(image) {
  // display slide/image on slideshow
  const imgElement = document.createElement('img');
  imgElement.src = 'images/' + image.name;
  const imageContainer = document.getElementById('slideshowContentContainer');
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);

  // display corresponding description text
  const paragraph = document.createElement('p');
  paragraph.appendChild(document.createTextNode(image.label));
  const descriptionContainer = document.getElementById('slideDescription');
  descriptionContainer.innerHTML = '';
  descriptionContainer.appendChild(paragraph);
}

/* Navigating the Slideshow with an index */
function updateSlideShowIndex(direction) {
  if (direction == DIRECTION_UP) {
    slideShowIndex = (slideShowIndex + 1) % images.length;
  }
  else if (direction == DIRECTION_DOWN) {
    slideShowIndex = slideShowIndex - 1;
    if (slideShowIndex < 0) {
      slideShowIndex += images.length;
    }
  }
  else if (direction == RANDOM_SLIDE) {
    slideShowIndex = Math.floor(Math.random() * images.length);
  }
  else {
    throw new Error('Invalid Parameter to updateSlideShowIndex() function');
  }
}

function togglePopup() {
  var popup = document.getElementById("popup");
  popup.classList.toggle("show");
  console.log(popup);
}
