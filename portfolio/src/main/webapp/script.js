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


const images =
    ['basketball.jpg', 'chess.jpg', 'crochet.jpg', 'smoothie.jpg', 'statefarm.jpg',
    'tennis.jpg', 'UIL_state.jpg', 'Uke.jpg'];

const descriptions =
    ['Playing basketball with the Tennis Intramural Team!', 'Playing chess with my roommate!',
    'Crocheting simple beanies!', 'Making delicious smoothies!', 'State Farm Internship!',
    'Tennis, with the Club Team at UT Austin!', 'Competitive Programming!', 'Playing the Ukulele!'];

const DIRECTION_UP = 'up';
const DIRECTION_DOWN = 'down';
const RANDOM_SLIDE = 'random';

let slideShowIndex = 0;

/* Onload, initalize slide show with an image */
function initializeSlideShow() {
  // display the first image in the slideshow
  let firstImageURL = 'images/basketball.jpg';
  let firstDescription = 'Playing basketball with the Tennis Intramural Team!';
  displayNewSlide(firstImageURL, firstDescription);
}

/* Navigate to the slide at slideShowIndex - 1 */
function previousSlide() {
  updateSlideShowIndex(DIRECTION_DOWN);
  let imageURL = 'images/' + images[slideShowIndex];
  let descriptionText = descriptions[slideShowIndex];
  displayNewSlide(imageURL, descriptionText);
}

/* Navigate to the slide at slideShowIndex + 1 */
function nextSlide() {
  updateSlideShowIndex(DIRECTION_UP);
  let imageURL = 'images/' + images[slideShowIndex];
  let descriptionText = descriptions[slideShowIndex];
  displayNewSlide(imageURL, descriptionText);
}

/* Navigate to a random slide */
function randomSlide() {
  updateSlideShowIndex(RANDOM_SLIDE);
  let imageURL = 'images/' + images[slideShowIndex];
  let descriptionText = descriptions[slideShowIndex];
  displayNewSlide(imageURL, descriptionText);
}

/* Display slideshow, with correctly navigated content */
function displayNewSlide(imageURL, descriptionText) {
  // display slide/image on slideshow
  const imgElement = document.createElement('img');
  imgElement.src = imageURL;
  const imageContainer = document.getElementById('slideshowContentContainer');
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);

  // display corresponding description text
  const paragraph = document.createElement('p');
  paragraph.appendChild(document.createTextNode(descriptionText));
  const descriptionContainer = document.getElementById('slideDescription');
  descriptionContainer.innerHTML = '';
  descriptionContainer.appendChild(paragraph);
  console.log(paragraph);
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
}

function togglePopup() {
  var popup = document.getElementById("popup");
  popup.classList.toggle("show");
  console.log(popup);
}
