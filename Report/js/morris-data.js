$(function()
 {$(function() { });
    Morris.Donut({  
      element: 'morris-donut-chart',
       data: [{    
        label: "Mutants morts nés",
         value: 0        }, {            label: "Mutants tués",            value: 5        }, {            label: "Mutants ayant survécus",   value: 24        }],        resize: true    }); 
 ;});