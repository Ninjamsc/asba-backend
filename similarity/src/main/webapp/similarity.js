var SIMILARITY_THRESHOLD = 0.8;

var photos = {
    photo1: null,
    photo2: null
};

var doResize;

$(document).ready(function() {
    ScaleFacePanels();
    $('[data-toggle="tooltip"]').tooltip();
    $('#photo0').change( function() {
        readURL1(this);
        $('#photo0_wrapper').attr('data-text', $(this).val());
        $('#submit_button').removeClass('pre1');
        if ($('#submit_button').hasClass('pre2') == false) {
            $('#submit_button').prop('disabled', false);
        }
    });
    $('#photo1').change( function() {
        readURL2(this);
        $('#photo1_wrapper').attr('data-text', $(this).val());
        $('#submit_button').removeClass('pre2');
        if ($('#submit_button').hasClass('pre1') == false) {
            $('#submit_button').prop('disabled', false);
        }
    });
});

$(window).resize(function(){
    $('.face-detection').each(function() {
        $(this).hide();
    });
    clearTimeout(doResize);
    doResize = setTimeout(onResize, 100);
});

function onResize() {
    ScaleFacePanels();
    // if (photos.photo1) {
    //     TrackImage(1);
    // }
    // if (photos.photo2) {
    //     TrackImage(2);
    // }
}

function ScaleFacePanels() {
    $('.face-panel').each(function() {
        var body = $(this).find('.panel-body');
        body.height(body.width());
        var faceCircle = $(this).find('.face-placeholder');
        var faceCircleSize = body.height() * 0.85;
        faceCircle.width(faceCircleSize);
        faceCircle.height(faceCircleSize);
        var face = $(this).find('.gray-face');
        face.css('font-size', faceCircleSize * 0.9);
        face.css('padding-top', faceCircleSize * 0.12);
    });
}

function ScaleImage(image) {
    if (image[0].naturalHeight > image[0].naturalWidth) {
        image.addClass('portrait');
        image.removeClass('landscape');
    } else {
        image.addClass('landscape');
        image.removeClass('portrait');
    }
}

function TrackImage(photo_idx) {
    var faceRect = $('#face' + photo_idx);
    faceRect.hide();
    var image = $('#image' + photo_idx);
    if (image.width() === 0) return;
    image.faceDetection({
        complete: function (faces) {
            var maxConfidence_idx = 0;
            for (var i=0; i < faces.length; i++) {
                var currentFace = faces[i];
                if (currentFace.confidence > faces[maxConfidence_idx].confidence) {
                    maxConfidence_idx = i;
                }
            }
            faceDetection= faces[maxConfidence_idx];
            if (faceDetection) {
                faceRect.css({
                    left: image.offset().left + faceDetection.x * faceDetection.scaleX,
                    top: image.offset().top + faceDetection.y * faceDetection.scaleY,
                    width: faceDetection.width * faceDetection.scaleX,
                    height: faceDetection.height * faceDetection.scaleY
                });
                faceRect.show();
            }
        },
        error: function (code, message) {
            // TODO
        }
    })
}

function LoadImageFromPC(event, photo_idx) {
    // Fix for various browsers
    // http://www.quirksmode.org/js/events_properties.html
    var fileChooser;
    if (event.target) fileChooser = event.target;
    else if (event.srcElement) fileChooser = event.srcElement;
    if (fileChooser.nodeType === 3) // defeat Safari bug
        fileChooser = fileChooser.parentNode;

    // http://blog.teamtreehouse.com/reading-files-using-the-html5-filereader-api
    var file = fileChooser.files[0];

    if (file) {
        var fileInputName = $('#file-input' + photo_idx);
        fileInputName.val(fileChooser.value);

        var reader = new FileReader();
        reader.onload = function (e) {
            photos['photo' + photo_idx] = e.target.result;
            CheckPhotos();
            var image = $('#image' + photo_idx);
            image.hide();
            var faceRect = $('#face' + photo_idx);
            faceRect.hide();
            image.attr('src', window.URL.createObjectURL(file));
        }
        reader.readAsDataURL(file);
    }
}

function CheckPhotos() {
    var submitButton = $('#submit-button');
    if (photos.photo1 && photos.photo2) {
        submitButton.prop('disabled', false);
    } else {
        submitButton.prop('disabled', true);
    }
}

function onImageLoaded(photo_idx) {
    var image = $('#image' + photo_idx);
    var placeholder = $('#placeholder' + photo_idx);
    placeholder.hide();
    ScaleImage(image);
    image.show();
    // setTimeout(function() {
    //     TrackImage(photo_idx);
    // }, 100);
}

function onImageLoadingError(photo_idx) {
    photos['photo' + photo_idx] = null;
    CheckPhotos();
    
    alert('Ошибка при загрузке фотографии');
    var image = $('#image' + photo_idx);
    var placeholder = $('#placeholder' + photo_idx);
    var faceRect = $('#face' + photo_idx);
    image.hide();
    placeholder.show();
    faceRect.hide();
}

function SubmitPhotos() {
    var alertWaiting = $('#alert-waiting');
    var alertSuccess = $('#alert-success');
    var alertFail = $('#alert-fail');

    if (photos.photo1 && photos.photo2) {
        alertWaiting.show();
        alertSuccess.hide();
        alertFail.hide();
        window.scrollTo(0,document.body.scrollHeight);

        $.ajax({
            async: true,
            url: 'api/compare',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({pictureA: photos.photo1, pictureB: photos.photo2}),
            dataType: 'json',
            success: function (result) {
                var similarity = result['similarity'];
                similarity = Math.round(similarity * 100) / 100;
                alertWaiting.hide();
                $('.result-value').each(function() {
                    $(this).text(similarity);
                    if (similarity < SIMILARITY_THRESHOLD) {
                        alertSuccess.hide();
                        alertFail.show();
                    } else {
                        alertFail.hide();
                        alertSuccess.show();
                    }
                });

            },
            error: function (jqXHR) {
                alert('Ошибка при обращении к серверу');
                alertWaiting.hide();
                alertFail.hide();
                alertSuccess.hide();
            },
        });
    } else {
        alert('Ошибка при отправке изображений');
    }
};