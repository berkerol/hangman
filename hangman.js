/* global names getSvg createTextRow createCheckboxRow createButtonGroupRow createAlert keyDownHandler keyUpHandler */
const defaultDel = true;
let del;

let sticks;
let name;
let nameIndex;
let hiddenName;
let oldLetters;

window.locked = true;

const col = document.getElementById('col');
col.appendChild(createTextRow('d-flex', 'mb-3', [['Guess', 'guess']]));
col.appendChild(createButtonGroupRow('d-flex', 'btn-group', [['success', 'if(!locked)window.guess()', 'g', 'search', '<u>G</u>uess'], ['primary', 'if(!locked)random()', 'r', 'random', '<u>R</u>andom']]));
col.appendChild(createButtonGroupRow('d-flex', 'btn-group mt-4', [['danger', 'if(!locked)giveUp()', 'u', 'times', 'Give <u>U</u>p'], ['info', 'restart()', 'e', 'sync', 'R<u>e</u>start']]));
col.appendChild(createCheckboxRow('d-flex', 'mb-3 mt-4', [['<u>D</u>elete name after round', 'del', 'd']]));
resetInputs();
restart();
document.addEventListener('keydown', keyDownHandler);
document.addEventListener('keyup', keyUpHandler);

function resetInputs () {
  del = defaultDel;
  document.getElementById('del').checked = del;
}

window.guess = function () {
  const input = document.getElementById('guess');
  const guess = input.value.toLowerCase();
  input.value = '';
  check(guess);
};

window.random = function () {
  let letter;
  do {
    letter = String.fromCharCode(97 + Math.floor(Math.random() * 26));
  } while (oldLetters.includes(letter));
  check(letter);
};

window.giveUp = function () {
  end();
};

function restart () {
  del = document.getElementById('del').checked;
  nameIndex = Math.floor(Math.random() * names.length);
  name = names[nameIndex];
  hiddenName = name.replace(/\w/g, '*');
  document.getElementById('name').innerHTML = hiddenName;
  sticks = 0;
  draw(sticks);
  oldLetters = [];
  document.getElementById('text').innerHTML = '';
  window.locked = false;
}

function check (letter) {
  if (letter.length === 1) {
    if (letter.charCodeAt() >= 97 && letter.charCodeAt() <= 122) {
      if (!oldLetters.includes(letter)) {
        oldLetters.push(letter);
        let letterExists = false;
        for (let i = 0; i < name.length; i++) {
          if (name.charAt(i).toLowerCase() === letter) {
            hiddenName = hiddenName.substring(0, i) + name.charAt(i) + hiddenName.substring(i + 1);
            letterExists = true;
          }
        }
        if (letterExists) {
          createAlert('success', `${letter} exists.`);
          document.getElementById('name').innerHTML = hiddenName;
          if (hiddenName === name) {
            end('success', 'Congratulations, name is completed.');
          }
        } else {
          draw(++sticks, 'warning', `${letter} does not exist.`);
        }
      } else {
        createAlert('danger', 'Already guessed before.');
      }
    } else {
      createAlert('danger', 'Not a letter.');
    }
  } else if (letter.length > 1) {
    if (letter.length === name.length) {
      if (letter === name.toLowerCase()) {
        end('success', 'Congratulations, your guess is correct.');
      } else {
        draw(++sticks, 'warning', 'Your guess is not correct.');
      }
    } else {
      createAlert('danger', 'Wrong number of letters.');
    }
  }
}

function end (className, text) {
  if (className) {
    createAlert(className, text);
  }
  document.getElementById('name').innerHTML = name;
  if (del) {
    names.splice(nameIndex, 1);
  }
  window.locked = true;
  createAlert('info', 'Restart the game!');
}

function draw (sticks, className, text) {
  if (className) {
    createAlert(className, text);
  }
  document.getElementById('image').src = getSvg(sticks);
  if (sticks === 8) {
    end('danger', 'You lost, figure is completed.');
  }
}
