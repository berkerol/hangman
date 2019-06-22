/* global names getSvg */
const defaultDel = true;
let del = defaultDel;

let sticks;
let name;
let nameIndex;
let hiddenName;
let oldLetters;
let locked;

document.getElementById('del').checked = del;
restart();
document.addEventListener('keydown', keyDownHandler);
document.addEventListener('keyup', keyUpHandler);

function guess () {
  const input = document.getElementById('guess');
  const guess = input.value.toLowerCase();
  input.value = '';
  check(guess);
}

window.random = function () {
  let letter;
  do {
    letter = String.fromCharCode(97 + Math.floor(Math.random() * 26));
  } while (oldLetters.includes(letter));
  check(letter);
};

window.giveUp = function () {
  exit();
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
  locked = false;
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
          write('alert alert-success', `${letter} exists.`);
          document.getElementById('name').innerHTML = hiddenName;
          if (hiddenName === name) {
            exit('alert alert-success', 'Congratulations, name is completed.');
          }
        } else {
          draw(++sticks, 'alert alert-warning', `${letter} does not exist.`);
        }
      } else {
        write('alert alert-danger', 'Already guessed before.');
      }
    } else {
      write('alert alert-danger', 'Not a letter.');
    }
  } else if (letter.length > 1) {
    if (letter.length === name.length) {
      if (letter === name.toLowerCase()) {
        exit('alert alert-success', 'Congratulations, your guess is correct.');
      } else {
        draw(++sticks, 'alert alert-warning', 'Your guess is not correct.');
      }
    } else {
      write('alert alert-danger', 'Wrong number of letters.');
    }
  }
}

function exit (className, text) {
  if (className) {
    write(className, text);
  }
  document.getElementById('name').innerHTML = name;
  if (del) {
    names.splice(nameIndex, 1);
  }
  locked = true;
  write('alert alert-info', 'Restart the game!');
}

function write (className, text) {
  const child = document.createElement('div');
  child.className = className + ' alert-dismissible fade show';
  child.innerHTML = '<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>' + text;
  const parent = document.getElementById('text');
  parent.insertBefore(child, parent.firstChild);
}

function draw (sticks, className, text) {
  if (className) {
    write(className, text);
  }
  document.getElementById('image').src = getSvg(sticks);
  if (sticks === 8) {
    exit('alert alert-danger', 'You lost, figure is completed.');
  }
}

function keyDownHandler (e) {
  if (e.keyCode === 13 && !locked) {
    e.preventDefault();
    guess();
  }
}

function keyUpHandler (e) {
  if (e.keyCode === 82) {
    del = defaultDel;
    document.getElementById('del').checked = del;
  }
}
