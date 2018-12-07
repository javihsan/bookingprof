	<section id="newLocalTask" class="splash">
		 <header>
            <nav class="right">
                <a href="#" class="button" data-action="new" data-icon="plus"><abbr data-lnt-id="form.add"></abbr></a>
            </nav>
             <nav class="left">
                <a href="#" class="button" data-action="combi" data-icon="tags"><abbr data-lnt-id="localTask.combi"></abbr></a>
                <a href="#" class="button" data-action="single" data-icon="tag"><abbr data-lnt-id="localTask.single"></abbr></a>
            </nav>
        </header>
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="localTask-form" class="list indented scroll active">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="localTask.cabTextNew"></span></li>
        		<li>
        			<label data-lnt-id="localTask.selectSystemTask"></label>:
   	   				<label class="select">
     					<select id="lotTaskId" class="custom"></select>
  					</label>
        		</li>
        		<li>
	   				<label data-lnt-id="localTask.visible"></label>:
	   				<label class="select">
     					<select id="lotVisible" class="custom">
     						<option value="1" data-lnt-id="general.yes"></option>
     						<option value="0" data-lnt-id="general.no"></option>
         				</select>
  					</label>
       			</li>
              	<li>
              		<label id="lotNameError" style="color:red;"></label> <label data-lnt-id="localTask.name"/></label>
              		<div id="localTask-name"></div>
              	</li>
              	<li>
        			<label id="lotTaskDurationError" style="color:red;"></label> <label data-lnt-id="localTask.duration"></label>:
        			<input id="lotTaskDuration" type="number" value="" pattern="\d{1,3}" required />
        		</li>
        		<li>
        			<label id="lotTaskPostError" style="color:red;"></label> <label data-lnt-id="localTask.post"></label>:
        			<input id="lotTaskPost" type="number" value="" pattern="\d{1,3}" required />
        		</li>
        		<li>
              		<label id="lotTaskRateError" style="color:red;"></label> <label data-lnt-id="localTask.rate"/></label> <span id="symbol_currency"></span> (nnnn,dd):
	 				<input id="lotTaskRate" size="5" type="number" value="" pattern="\d{1,4}([,|.]\d{1,2})?" required>
        		</li>
           	</ul>
         </article>
         
         <article id="localTaskCombi-form" class="list form indented scroll">
         </article>
        
    </section>

	<script src="/app/view/localTaskNameView.js"></script>
	<script src="/app/view/localTaskCombiView.js"></script>
	<script src="/app/view/localTaskCombiFirstView.js"></script>
    <script src="/app/controller/localTaskNewCtrl.js"></script>
    <script src="/app/controller/localTaskCombiNewCtrl.js"></script>
 