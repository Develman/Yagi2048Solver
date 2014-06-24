function Ai() {
    this.init = function() {}
    this.restart = function() {}

    this.step = function(grid) { 
		// 0: up, 1: right, 2: down, 3: left
		
		var gridJson = createJson(grid);
		console.log(gridJson);
		
		return loadNextStep(gridJson);
    };
    
    function loadNextStep(grid) {
		var nextStep = -1;
		
		$.ajax({
			async: false,
			url : "http://localhost:8080/YagiSolver/ws",
			type: "POST",
			contentType: "application/json; charset=utf-8",
			data : grid,
			dataType: "json"
		}).done(function(data) {
			console.log(data);
			nextStep = data.value.trim();
		}).fail(function(jqr) {
			console.log(jqr);
		});	
		
		return nextStep;
	}
	
	function createJson(grid) {
		var exportGrid = new ExportGrid(grid.size);
		grid.eachCell(function(x, y, cell) {
			if (cell) {
				exportGrid.addValue(x, y, cell.value);
			} else {
				exportGrid.addValue(x, y, 0);
			}
		});
		
		return JSON.stringify(exportGrid);
	}
    
    function ExportGrid(size) {
		this.columns = [];
		this.initColumns(size);
	}
	
	ExportGrid.prototype.initColumns = function(size) {
	  for (var x = 0; x < size; x++) {
		var column = this.columns[x] = [];
		
		for (var y = 0; y < size; y++) {
			column.push(0);
		}
	  }
	};
	
	ExportGrid.prototype.addValue = function(posX, posY, value) {
		this.columns[posX][posY] = value;
	};
}
