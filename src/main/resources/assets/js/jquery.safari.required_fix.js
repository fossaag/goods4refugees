/**
 * Fossa AG, Jens Vogel, bloody hack to fix the 'required' issue with Safari and Opera
 */

$("form").submit(function(e) {

    var ref = $(this).find("[required]");

    $(ref).each(function(){
        if ( $(this).val() == '' )
        {
            alert("Bitte fülle das Feld aus.");
            $(this).focus();
            e.preventDefault();
            return false;
        }
    });  return true;
});