	
    <section id="newFirm" class="splash">
               
        <footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="firm-form" class="list indented scroll active">
        	<ul class="form">
           		<li class="dark"><span data-lnt-id="firm.cabText"></span></li>
               	<li>
        			<label data-lnt-id="firm.name"></label>:
        			<input type="text" data-lnt-id="firm.name" placeholder="" id="firName" value="" />
        		</li>
        		<li>
        			<label data-lnt-id="firm.domain"></label>:
        			<input type="text" data-lnt-id="firm.domain" placeholder="" id="firDomain" value="" />
        		</li>
        		<li>
        			<label data-lnt-id="firm.server"></label>:
        			<input type="text" data-lnt-id="firm.server" placeholder="" id="firServer" value="" />
        		</li>
        		<li>
        			<label data-lnt-id="firm.billedModule"></label>:
   	   				<label class="select">
     					<select id="firBilledModule" class="custom">
     						<option value="0" data-lnt-id="general.no"></option>
     						<option value="1" data-lnt-id="general.yes" selected></option>
         				</select>
  					</label>
        		</li>
        		<li>
        			<label data-lnt-id="firm.config"></label>:
   	   				<input type="text" data-lnt-id="firm.config" placeholder="" id="firConfig" value="" />
        		</li>
        		<li>
        			<label data-lnt-id="firm.classTasks"></label>:
   	   				<select style="height: 90px;" id="firClassTasks" class="custom" multiple>
        			</select>
  		   		</li>
  		   		<li data-icon="home">
					<label data-lnt-id="where.adddress"></label>:
        			<input type="text" data-lnt-id="where.adddress" placeholder="" id="firAddress" value="" />
					<label data-lnt-id="where.city"></label>:
        			<input type="text" data-lnt-id="where.city" placeholder="" id=firCity value="" />
					<label data-lnt-id="where.state"></label>:
        			<input type="text" data-lnt-id="where.state" placeholder="" id="firState" value="" />
        		</li>
        		<li>
					<label data-lnt-id="where.cp"></label>:
        			<input type="text" data-lnt-id="where.cp" placeholder="" id="firCP" value="" />
        		</li>
        		<li>
					<label data-lnt-id="where.country"></label>:
        			<input type="text" data-lnt-id="where.country" placeholder="" id="firCountry" value="" />
        		</li>
        		<li data-icon="user">
        			<label data-lnt-id="professional.name"></label>:
        			<input type="text" data-lnt-id="professional.name" placeholder="" id="firResponName" value="" />
        			<label data-lnt-id="professional.surname"></label>:
        			<input type="text" data-lnt-id="professional.surname" placeholder="" id="firResponSurname" value="" />
        			<label data-lnt-id="professional.email"></label>:
        			<input type="text" data-lnt-id="professional.email" placeholder="" id="firResponEmail" value="" />
        			<label data-lnt-id="professional.telf"></label>:
        			<input type="text" data-lnt-id="professional.telf" placeholder="" id="firResponTelf1" value="" />
        		</li>
        		<li>
					<label data-lnt-id="firm.guser"></label>:
        			<input type="text" data-lnt-id="firm.guser" placeholder="" id="firGwtUsers" value="" />
        		</li>
        	</ul>
        </article>
   
    </section>
 
    <script src="/web/controller/firmNewAdminCtrl.js"></script>
  
 