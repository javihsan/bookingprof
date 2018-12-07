
	<section id="newClient" class="splash">
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="client-form" class="list indented scroll active">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="client.cabText"></span></li>
              	<li>
        			<label id="error_cliName" style="color:red;"></label> <label data-lnt-id="client.name"></label>:
        			<input type="text" data-lnt-id="client.name" placeholder="" id="cliName" required/>
        		</li>
	       		<li>
        			<label data-lnt-id="client.surname"></label>:
        			<input type="text" data-lnt-id="client.surname" placeholder="" id="cliSurname"/>
        		</li>
    			<li>
        			<label id="error_cliEmail" style="color:red;"></label> <label data-lnt-id="client.email"></label>:
        			<input type="email" data-lnt-id="client.email" placeholder="" id="cliEmail" />
        		</li>
        		<li>
        			<label id="error_cliGender" style="color:red;"></label> <label data-lnt-id="client.gender"></label>:
   	   				<label class="select">
     					<select id="cliGender" class="custom">
     						<option value="-1" data-lnt-id="general.undefined"></option>
     						<option value="0" data-lnt-id="general.male"></option>
     						<option value="1" data-lnt-id="general.female"></option>
         				</select>
  					</label>
        		</li>
        		<li>
        			<label id="error_cliBirthday" style="color:red;"></label> <label data-lnt-id="client.birthday"></label>: yyyy-mm-dd
        			<input id="cliBirthday" size="5" type="date">
        		</li>
    			<li>
        			<label id="error_cliTelf1" style="color:red;"></label> <label data-lnt-id="client.movil"></label>:
        			<input type="tel" placeholder="6.." id="cliTelf1" />
        		</li>
        		<li>
        			<label id="error_cliTelf2" style="color:red;"></label> <label data-lnt-id="client.telf"></label>:
        			<input type="tel" placeholder="9.." id="cliTelf2" />
        		</li>
        		<li>
        			<label data-lnt-id="client.desc"></label>:
        			<textarea data-lnt-id="client.desc" placeholder="" id="cliDesc" maxlength="300"></textarea>
        		</li>
        		<li class="dark"><span data-lnt-id="client.historyVisits"></span></li>
        	</ul>
        	<ul>	
        	
	       </ul>
        </article>
        
    </section>

    <script src="/app/controller/clientNewCtrl.js"></script>
 