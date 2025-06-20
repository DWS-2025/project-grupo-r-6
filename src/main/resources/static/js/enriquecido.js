function format(command) {
    if (command === 'createLink') {
        const url = prompt("Introduce la URL:");
        if (url) document.execCommand(command, false, url);
    } else {
        document.execCommand(command, false, null);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('commentForm');
    form.addEventListener('submit', function () {
        const htmlContent = document.getElementById('editor').innerHTML;
        document.getElementById('message').value = htmlContent;
    });
});
