	<section id="newProduct" class="splash">
		 <header>
            <nav class="right">
                <a href="#" class="button" data-action="new" data-icon="plus"><abbr data-lnt-id="form.add"></abbr></a>
            </nav>
        </header>
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="product-form" class="list indented scroll active">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="product.cabTextNew"></span></li>
              	<li>
              		<label id="proNameError" style="color:red;"></label> <label data-lnt-id="product.name"/></label>
              		<div id="product-name"></div>
              	</li>
        		<li>
              		<label id="proRateError" style="color:red;"></label> <label data-lnt-id="product.rate"/></label> <span id="symbol_currency"></span> (nnnn,dd):
	 				<input id="proRate" size="5" type="number" value="" pattern="\d{1,4}([,|.]\d{1,2})?" required>
        		</li>
           	</ul>
         </article>
        
    </section>

	<script src="/app/view/productNameView.js"></script>
    <script src="/app/controller/productNewCtrl.js"></script>
 