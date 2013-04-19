$(document).ready(function(){
    initializeAutoComplete();
    initializeSearchButton();
})

/**
 * Initializes the search query box autocomplete to query the /suggest url for
 * suggestions.
 */
function initializeAutoComplete() {
    $("#search-query").typeahead({source:sourceFunction})

    function sourceFunction() {
        var suggestUrl = "/suggest?query=" + $('#search-query').val();
        var suggestions;

        function getSuggestions(suggestResult) {
            suggestions = suggestResult.results;
        }

        function handleError(jqXHR, textStatus, errorThrown) {
            suggestion = [];
        }
        
        $.ajax({
            url: suggestUrl,
            dataType: 'json',
            success: getSuggestions,
            error: handleError,
            async: false
        });
        
        return suggestions;
    }
}

/**
 * Initializes the search button to show the first page with results on click.
 */
function initializeSearchButton() {
    $("#search-form").submit(function() {
        query(1);
        return false;
    });
}

/**
 * Constructs the pagination html, it's in the form
 *
 * [1] ... [3][4][5][6][7][8][9][10] ... [20]
 */
function paginationHtml(queryResult) {
    if (queryResult.totalResults == 0) return "";

    function pageHtml(page, active) {
        return (active ? '<li class="active">' : '<li>') +
                '<a href="#" onclick="query(' + page + ')" >' + page + '</a></li>';
    }
    
    var html = '<div class="pagination"><ul>';

    var page = queryResult.currentPage;
    var first = Math.max(1, page - 5);
    var totalPages = Math.ceil(queryResult.totalResults / queryResult.perPage);
    var last = Math.min(totalPages, page + 5 + 5 - (page - first));

    if (last == totalPages) {
        first = Math.max(1, last - 10);
    }

    if (totalPages > 10 && first != 1) {
        html += pageHtml(1, false);
        html += pageHtml('...', true);
    }
    
    for (var page = first; page <= last; page++) {
        html += pageHtml(page, (page == queryResult.currentPage));
    }

    if (totalPages > 10 && last != totalPages) {
        html += pageHtml('...', true);    
        html += pageHtml(totalPages, false);
    }

    html += "</ul></div>";

    return html;
}

/**
 * Class that facilitates building the html for a table.
 */
function TableBuilder(columns) {
    this.html = '<table class="table table-striped">';
    this.html += '<thead>';
    this._addRow(columns, 'th');
    this.html += '</thead><tbody>';
}

TableBuilder.prototype._addRow = function(values, columnTag) {
    var width = Math.floor(100 / values.length);
    this.html += '<tr>';
    for (var i = 0; i < values.length; i++) {
        this.html += '<' + columnTag + ' width="' + width + '%">' + values[i] + '</' + columnTag + '>';
    }
    this.html += '</tr>';
}

TableBuilder.prototype.addRow = function(values) {
    this._addRow(values, 'td');
}

TableBuilder.prototype.build = function() {
    this.html += "</tbody></table>";
    return this.html;
}

/**
 * Queries for a page with results.
 */
function query(page) {
    var queryUrl = "/query?query=" + $('#search-query').val() + "&page=" + page;

    function showResults(queryResult) {
        var results = queryResult.results;
        var html;

        if (results.length > 0) {
            var table = new TableBuilder(["Word", "Count"]);
            for (var i = 0; i < results.length; i++) {
                table.addRow([results[i].key, results[i].value]);
            }
            html = table.build();
        } else {
            html = "Your search <b>" + $('#search-query').val() + "</b> did not match any words.";
        }

        html += paginationHtml(queryResult);

        $('#results').html(html);
    }

    function handleError(jqXHR, textStatus, errorThrown) {
        $('#results').html(textStatus);
    }

    $.ajax({
        url: queryUrl,
        dataType: 'json',
        success: showResults,
        error: handleError,
        async: false
    });
}
