	<section id="newInvoice" class="splash">
		<header>
            <nav class="right">
                <a href="#" class="button" data-action="new" data-icon="plus"><abbr data-lnt-id="label.aside.localTasks"></abbr></a>
            </nav>
            <nav class="right">
                <a href="#" class="button" data-action="newPro" data-icon="plus"><abbr data-lnt-id="label.aside.products"></abbr></a>
            </nav>
        </header>
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="invoice-form" class="list form indented scroll active"></article>
 		
 		<article id="invoiceTaskSelect-form" class="list scroll form"></article>
        
    </section>

	<script src="/app/model/billedModel.js"></script>
	<script src="/app/model/invoiceModel.js"></script>
	<script src="/app/view/invoiceListView.js"></script>
	<script src="/app/view/invoiceListDayView.js"></script>
    <script src="/app/view/invoiceListUntilView.js"></script>
    <script src="/app/view/invoiceListFromView.js"></script>
	<script src="/app/view/billedView.js"></script>
	<script src="/app/view/invoiceView.js"></script>
	<script src="/app/view/invoiceNewView.js"></script>
	<script src="/app/view/localTaskInvView.js"></script>
	<script src="/app/view/localTaskInvSelectView.js"></script>
    <script src="/app/controller/invoiceNewCtrl.js"></script>
    <script src="/app/controller/invoiceListCtrl.js"></script>
    <script src="/app/controller/invoiceTaskSelectCtrl.js"></script>
