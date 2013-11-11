/************************************************************************
*************************************************************************
@Name :       	jRating - jQuery Plugin
@Revison :    	3.1
@Date : 		13/08/2013
@Author:     	 ALPIXEL - (www.myjqueryplugins.com - www.alpixel.fr)
@License :		 Open Source - MIT License : http://www.opensource.org/licenses/mit-license.php

**************************************************************************
*************************************************************************/
(function($) {
	$.fn.jDel = function(op) {
		alert("Ciao");
		if(this.length>0)
		return this.each(function() {
			/*vars*/
			
			var defaults = {
					idBox : 0,

					/** Functions **/
					onSuccess : null, // Fires when the server response is ok
					onError : null, // Fires when the server response is not ok
					onClick: null // Fires when clicking on a star
				};
			var opts = $.extend(defaults, op),
			newWidth = 0,
			starWidth = 0,
			starHeight = 0,
			bgPath = '',
			hasRated = false,
			globalWidth = 0,
			nbOfRates = opts.nbRates;
			
			if($(this).attr('disable')=='true')
				opts.isDisabled = true;
			else
				opts.isDisabled = false;	

			var jDisabled = false;

			idBox = parseInt($(this).attr('data-id')), // get the id of the box
			
			$(this).css({width: widthRatingContainer,overflow:'hidden',zIndex:1,position:'relative'});
			
			if(!jDisabled)
			$(this).unbind().bind({
				
				click : function(e){
                    var element = this;

					/*set vars*/
					hasRated = true;
					globalWidth = newWidth;
					nbOfRates--;
					

					if(!opts.canRateAgain || parseInt(nbOfRates) <= 0) $(this).unbind().css('cursor','default').addClass('jDisabled');

					if (opts.showRateInfo) $("p.jRatingInfos").fadeOut('fast',function(){$(this).remove();});
					e.preventDefault();
					var rate = getNote(newWidth);
					average.width(newWidth);

					averageVote = parseFloat($(this).attr('data-average'));

					/** ONLY FOR THE DEMO, YOU CAN REMOVE THIS CODE 
						$('.datasSent p').html('<strong>idBox : </strong>'+idBox+'<br /><strong>rate : </strong>'+rate+'<br /><strong>action :</strong> rating');
						$('.serverResponse p').html('<strong>Loading...</strong>');
					 END ONLY FOR THE DEMO **/
						
		                $.get('ReleaseDeleteServlet',{id:idBox},function(responseText) { 
		                        $('.del_'+idBox).html('<div>I dati della release sono stati eliminati!</div>');
		                });
					if(opts.onClick) opts.onClick( element, rate );

					if(opts.sendRequest) {
						$.post(opts.phpPath,{
								idBox : idBox,
								rate : rate,
								action : 'rating'
							},
							function(data) {
								if(!data.error)
								{
									/** ONLY FOR THE DEMO, YOU CAN REMOVE THIS CODE 
										$('.serverResponse p').html(data.server);
									 END ONLY FOR THE DEMO **/


									/** Here you can display an alert box,
										or use the jNotify Plugin :) http://www.myqjqueryplugins.com/jNotify
										exemple :	*/
									if(opts.onSuccess) opts.onSuccess( element, rate );
								}
								else
								{

									/** ONLY FOR THE DEMO, YOU CAN REMOVE THIS CODE 
										$('.serverResponse p').html(data.server);
									 END ONLY FOR THE DEMO **/

									/** Here you can display an alert box,
										or use the jNotify Plugin :) http://www.myqjqueryplugins.com/jNotify
										exemple :	*/
									if(opts.onError) opts.onError( element, rate );
								}
							},
							'json'
						);
					}

				}
			});

			function getNote(relativeX) {
				var noteBrut = parseFloat((relativeX*100/widthRatingContainer)*parseInt(opts.rateMax)/100);
				var dec=Math.pow(10,parseInt(opts.decimalLength));
				var note = Math.round(noteBrut*dec)/dec;
				return note;
			};

			function getStarWidth(){
				switch(opts.type) {
					case 'small' :
						starWidth = 12; // width of the picture small.png
						starHeight = 10; // height of the picture small.png
						bgPath = opts.smallStarsPath;
					break;
					default :
						starWidth = 23; // width of the picture stars.png
						starHeight = 20; // height of the picture stars.png
						bgPath = opts.bigStarsPath;
				}
			};

			function findRealLeft(obj) {
			  if( !obj ) return 0;
			  return obj.offsetLeft + findRealLeft( obj.offsetParent );
			};
		});

	}
})(jQuery);
