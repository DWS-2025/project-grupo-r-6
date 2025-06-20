
    const pageSize = 2;
    let currentPage = 0;

    function getCurrentShopID() {
        const pathParts = window.location.pathname.split('/');
        return pathParts[pathParts.length - 1]; // Asume que el ID de la tienda va al final de la URL
    }

    function loadComments(page) {
        const shopID = getCurrentShopID(); // Define esta función según tu lógica (por ejemplo, extraído de la URL o de un atributo)
        fetch(`/api/comments/paginated?shopID=${shopID}&page=${page}&size=${pageSize}`)
            .then(response => {
                if (!response.ok) throw new Error("Error al cargar comentarios");
                return response.json();
            })
            .then(data => {
                currentPage = data.number;
                renderComments(data.content);
                renderPagination(data.totalPages, data.number);
            })
            .catch(error => {
                document.getElementById("comments-container").innerHTML = `<p>Error: ${error.message}</p>`;
            });
    }


    function renderComments(comments) {
    const container = document.getElementById("comments-container");
    container.innerHTML = "";

    if (comments.length === 0) {
    container.innerHTML = "<p>No hay comentarios todavia.</p>";
    return;
}

    comments.forEach(comment => {
    const commentHTML = `<a href="/comments/${comment.shopID}/${comment.commentId}" class="comment-select">Asunto: ${comment.issue}</a><br>`;
    container.innerHTML += commentHTML;
});
}

    function renderPagination(totalPages, pageNumber) {
        const controls = document.getElementById("pagination-controls");
        controls.innerHTML = "";

        if (pageNumber > 0) {
            const prevBtn = document.createElement("button");
            prevBtn.innerText = "Anterior";
            prevBtn.onclick = () => loadComments(pageNumber - 1);
            controls.appendChild(prevBtn);
        }

        const pageText = document.createElement("span");
        pageText.innerText = ` Pagina ${pageNumber + 1} de ${totalPages} `;
        controls.appendChild(pageText);

        if (pageNumber < totalPages - 1) {
            const nextBtn = document.createElement("button");
            nextBtn.innerText = "Siguiente";
            nextBtn.onclick = () => loadComments(pageNumber + 1);
            controls.appendChild(nextBtn);
        }
    }


    // Cargar al iniciar
    window.onload = () => {
    loadComments(currentPage);
};

