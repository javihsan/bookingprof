
	<section id="cancelEvent" class="splash">
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>

		<article id="event-cancel-form" class="list scroll indented active">
			<ul class="form">
        		<li class="dark"><span data-lnt-id="event.cancelTitle"></span> <span id="event_hour"></span></li>
        		<li class="dark">
        			<label data-lnt-id="event.cancelText"></label>
        		</li>
        		<li>
	   				<label id="error_eveClientEmail" style="color:red;"></label>
	   			</li>
        		<li>
	   				<label data-lnt-id="event.cancelSend"></label>:
	   				<label class="select">
     					<select id="eveCancelSend" class="custom">
     						<option value="1" data-lnt-id="general.yes"></option>
     						<option value="0" data-lnt-id="general.no"></option>
         				</select>
  					</label>
       			</li>
       			<li>
       				<label data-lnt-id="event.cancelCom"></label>:
        			<textarea data-lnt-id="event.cancelCom" placeholder="" id="eveCancelText" maxlength="300"></textarea>
        		</li>
		     </ul>
		</article>
	 </section>

<script src="/app/controller/eventCancelCtrl.js"></script>