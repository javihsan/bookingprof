	
	<section id="booking-hours" class="splash">
	    <header>
	    	<!-- <nav class="left">
               <a href="#" class="button" data-action="festive" data-icon="suitcase"><abbr data-lnt-id="label.header.festives"></abbr></a>
            </nav> -->
            <nav class="right">
               <a href="#" class="button" data-action="today" data-icon="calendar"><abbr data-lnt-id="label.header.today"></abbr></a>
            </nav>
        </header>
		<footer>
        	<nav class="">
               <a href="#" data-action="return" data-icon="undo"><abbr data-lnt-id="form.return"></abbr></a>
            </nav>
        </footer>
              
 		<article id="hours" class="active booking_calendar_hours">
        	<!-- Inicio tableMonthBooking -->
			<%@include file="tableMonthBooking.jsp"%>
			<!-- Fin tableMonthBooking -->
        </article>
        
    </section>
 	
 	<script src="/app/controller/bookingHoursCtrl.js"></script>
    
