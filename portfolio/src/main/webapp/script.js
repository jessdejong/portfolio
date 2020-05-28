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
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}


const images =
    ['JessDeJong-photo.png', 'Profile_photo_Jess.jpg'];

let slideShowIndex = 0;

function initializeSlideShow() {
  // display the first image in the slideshow
  const imgElement = document.createElement('img');
  imgElement.src = 'images/JessDeJong-photo.png';
  const imageContainer = document.getElementById('slideshow-content-container');
  imageContainer.appendChild(imgElement);
}

function previousSlide() {
  updateSlideShowIndex('down');
  let imageURL = 'images/' + images[slideShowIndex];
  displayNewSlide(imageURL);
}

function nextSlide() {
  updateSlideShowIndex('up');
  let imageURL = 'images/' + images[slideShowIndex];
  displayNewSlide(imageURL);
}

function displayNewSlide(imageURL) {
  const imgElement = document.createElement('img');
  imgElement.src = imageURL;
  const imageContainer = document.getElementById('slideshow-content-container');
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);
}

function updateSlideShowIndex(direction) {
  if (direction == 'up') {
    slideShowIndex = (slideShowIndex + 1) % images.length;
  }
  else if (direction == 'down') {
    slideShowIndex = slideShowIndex - 1;
    if (slideShowIndex < 0) {
      slideShowIndex += images.length;
    }
  }
}
