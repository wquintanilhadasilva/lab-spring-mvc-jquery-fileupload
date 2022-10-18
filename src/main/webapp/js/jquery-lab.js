$(function() {

    var files = [];
	var rowIdx = 0;
	
	$("#faleConoscoForm").on("submit", function(event){
		
		event.preventDefault();
		
        var data = $('#faleConoscoForm').serializeJSON();
		data.arquivos = files;
        console.log(data);
    	var jsonData = JSON.stringify(data);
		console.log(jsonData);
        $.ajax({
            url: '/save',
            data: jsonData,
            cache: false,
            contentType: false,
            processData: false,
            method: 'POST',
            type: 'POST',
			dataType : 'json',
			contentType: 'application/json',
    
            success:function(data){
                console.log(data);
				files = [];
				tableRepaint();
				$('#faleConoscoForm').trigger("reset");
            },
            error: function(jqxhr, textStatus, errThrown){
                console.log(textStatus);
                console.log(errThrown);
            }
        });
    });

    $("#fileLoader").on("change", function(){

        var formTag = $("#import-form")[0];
        var formData = new FormData(formTag);
        formData.append("file", $(this)[0].files[0]);
    
        $.ajax({
            url: '/upload',
            data:formData,
            cache: false,
            contentType: false,
            processData: false,
            method: 'POST',
            type: 'POST',
    
            success:function(data){
                console.log(data);
				let result = JSON.parse(data);
				console.log(result);
				files.push(result);
				tableAddRow(result);
				$('#fileLoader').val('');
            },
            error: function(jqxhr, textStatus, errThrown){
                console.log(textStatus);
                console.log(errThrown);
            }
        });
    });


	$('#tbody').on('click', '.remove', function () {
		
		// Getting <tr> id.
        var id = $(this).attr('id');

	    var fileToRemove = files[id];
	    console.log(fileToRemove);

		$.ajax({
            url: `/delete/${fileToRemove.nome}`,
            method: 'DELETE',
            type: 'DELETE',
    
            success:function(data){
                console.log(data);
				files.splice(id,1);
				tableRepaint();

            },
            error: function(jqxhr, textStatus, errThrown){
                console.log(textStatus);
                console.log(errThrown);
            }
        });
  
    });

	function tableAddRow(data) {
		$('#tbody').append(`
		<tr>
          <td class="row-index text-center">
              <p>${data.nome}</p>
		  </td>
		  <td class="row-index text-center">
              <p>${data.size}</p>
		  </td>
       	  <td class="text-center">
              <button id="${rowIdx++}" class="btn btn-danger remove" type="button">Remover</button>
          </td>
        </tr>`);
	}
	
	function tableRepaint() {
		$('#tbody').empty();
		rowIdx = 0;
		files.forEach(item => {
			tableAddRow(item);
		});
	}

});