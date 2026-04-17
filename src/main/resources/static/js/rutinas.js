// Activa el modal de confirmacion de eliminacion pasandole el id y descripcion
document.addEventListener('DOMContentLoaded', function () {
    var botonesEliminar = document.querySelectorAll('[data-bs-target="#confirmarEliminarModal"]');
    botonesEliminar.forEach(function (boton) {
        boton.addEventListener('click', function () {
            var id = this.getAttribute('data-bs-id');
            var descripcion = this.getAttribute('data-bs-descripcion');
            document.getElementById('campoIdModal').value = id;
            document.getElementById('descripcionModal').textContent = descripcion;
        });
    });
});
