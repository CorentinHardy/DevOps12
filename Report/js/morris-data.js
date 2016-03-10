$(function()
 {$(function() { });
    Morris.Donut({  
      element: 'morris-donut-chart',
       data: [{    
        label: "Mutants morts nés",
         value: 1        }, {            label: "Mutants tués",            value: 1        }, {            label: "Mutants ayant survécus",   value: 0        }],        resize: true    }); 
 ;});