
	<section id="finEvent" class="splash">
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>

		<article id="event-fin-form" class="list scroll indented active">
			<ul class="form">
        		<li class="dark"><span data-lnt-id="event.finTitle"></span> <span id="event_hour"></span></li>
        		<li class="dark">
        			<label data-lnt-id="event.finText"></label>
        		</li>
        		<li>
	   				<label data-lnt-id="event.finSel"></label>:
	   				<label class="select">
     					<select id="eveFinSel" class="custom">
     						<option value="1" data-lnt-id="event.finFin"></option>
     						<option value="2" data-lnt-id="event.finReject"></option>
         				</select>
  					</label>
       			</li>
       			<li>
       				<label data-lnt-id="event.finCom"></label>:
        			<textarea data-lnt-id="event.finCom" placeholder="" id="eveFinText" maxlength="300"></textarea>
        		</li>
		     </ul>
		</article>
	 </section>

<script src="/app/controller/eventFinCtrl.js"></script>