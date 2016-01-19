window.onload = function() {
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        // paginationClickable: true,
        // nextButton: '.swiper-button-next',
        // prevButton: '.swiper-button-prev',
        autoplay: 3000,
        // slidesPerView: 'auto',
        autoplayDisableOnInteraction: false,
        direction: 'horizontal',
        loop: true
    });
}
