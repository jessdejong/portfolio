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

const TRANSFORM = 'transform';
const FILTER = 'filter';
const RESET = 'reset';

// image manipulation values
let rotationAngle = 0;
let fractionInverted = 0;
let radiusBlurred = 0;
let brightness = 1;
let grayscale = 0;
let hueValue = 0;
let opacityValue = 1;

/**
 * Functions that manipulate
 * my profile picture.
 */

function rotateProfilePicture() {
  rotationAngle += 90;
  let value = 'rotate(' + rotationAngle + 'deg)';
  changeProfilePicture(TRANSFORM, value);
}

function invertProfilePicture() {
  fractionInverted += .2;
  let value = 'invert(' + fractionInverted + ')';
  changeProfilePicture(FILTER, value);
}

function blurProfilePicture() {
  radiusBlurred += 2;
  let value = 'blur(' + radiusBlurred + 'px)';
  changeProfilePicture(FILTER, value);
}

function brightenProfilePicture() {
  brightness += .2;
  let value = 'brightness(' + brightness + ')';
  changeProfilePicture(FILTER, value);
}

function grayProfilePicture() {
  grayscale += .2;
  let value = 'grayscale(' + grayscale + ')';
  changeProfilePicture(FILTER, value);
}

function hueRotateProfilePicture() {
  hueValue += 45;
  let value = 'hue-rotate(' + hueValue + 'deg)';
  changeProfilePicture(FILTER, value);
}

function opacityProfilePicture() {
  opacityValue -= .2;
  let value = 'opacity(' + opacityValue + ')';
  changeProfilePicture(FILTER, value);
}


/* Reset the profile picture */
function resetProfilePicture() {
  let value = 'none';
  changeProfilePicture(RESET, value);
}

/* Performs the image manipulation */
function changeProfilePicture(property, value) {
  let image = document.getElementById('profilePicture');
  switch(property) {
    case TRANSFORM: 
      image.style.transform = value;
      break;
    case FILTER:
      image.style.filter = value;
      break;
    case RESET:
      image.style.transform = value;
      image.style.filter = value;
      break;
    default:
      throw new Error('Invalid parameter passed to changeProfilePicture function');
  }
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
  switch (direction) {
    case DIRECTION_UP:
      slideShowIndex = (slideShowIndex + 1) % images.length;
      break;

    case DIRECTION_DOWN:
      slideShowIndex = slideShowIndex - 1;
      if (slideShowIndex < 0) {
        slideShowIndex += images.length;
      }
      break;

    case RANDOM_SLIDE:
      slideShowIndex = Math.floor(Math.random() * images.length);
      break;

    default:
      throw new Error('Invalid Parameter to updateSlideShowIndex() function');
  }
}

function togglePopup() {
  var popup = document.getElementById("popup");
  popup.classList.toggle("show");
  console.log(popup);
}

/* Fetch comments from the Server and add to DOM */
function showComments() {
  fetch('/data').then(response => response.json()).then(comments => {
    const commentsList = document.getElementById("commentsContainer");
    commentsList.innerHTML = '';
    for (let i = 0; i < comments.length; i++) {
      commentsList.appendChild(createListElement(comments[i]));
    }
  });
}

/* Create a list element that contains a comment text */
function createListElement(comment) {
  const listElement = document.createElement('li');
  listElement.innerHTML = comment;
  return listElement;
}
