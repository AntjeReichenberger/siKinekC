<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Search for KinekPoints</s:layout-component>

	<s:layout-component name="body">	
	<link rel="stylesheet" href="resource/css/pages/depotsearch.css" type="text/css" media="screen,projection" />
	
	<script type="text/javascript">
	jQuery(document).ready(function(){
		jQuery('#nav li:eq(5)').addClass('active');

		jQuery('#borderLink').click(function(e) {
			e.preventDefault();
			showBorderSearch();
		});

		jQuery('#normalLink').click(function(e) {
			e.preventDefault();
			hideBorderSearch();
		});

		jQuery('#borderProvince').change(function() {
			jQuery('#borderProvinceId').val(jQuery('#borderProvince').val());
		});

		if(jQuery('#isBorderSearch').val() == 'true') {
			showBorderSearch();
		}
	});

	function showBorderSearch() {
		jQuery('#basicInstructions').hide();
		jQuery('#basicSearch').hide();
		jQuery('#borderInstructions').show();
		jQuery('#findDeliveryLocation').hide();
		jQuery('#borderSearch').show();
		jQuery('#borderProvinceId').val(0);

		jQuery('#message').hide();
	}

	function hideBorderSearch() {
		jQuery('#basicInstructions').show();
		jQuery('#basicSearch').show();
		jQuery('#borderInstructions').hide();
		jQuery('#findDeliveryLocation').show();
		jQuery('#borderSearch').hide();
		jQuery('#borderProvinceId').val(0);

		jQuery('#message').hide();
	}

	function validateSearch()
	{
		var message = "<div id=\"message\" class=\"error\">";
		var isValid = true;

		if (document.getElementById('postalCode').value == "" && document.getElementById('province').options[document.getElementById('province').selectedIndex].value != "" && document.getElementById('city').value == "") {
			isValid = false;
			message += "<p>Please type in the name of a City to continue</p>";
		}

		if (document.getElementById('postalCode').value == "" && document.getElementById('province').options[document.getElementById('province').selectedIndex].value == "" && document.getElementById('city').value != "") {
			isValid = false;
			message += "<p>Please enter State/Province</p>";
		}

		if (document.getElementById('postalCode').value == "" && document.getElementById('province').options[document.getElementById('province').selectedIndex].value == "" && document.getElementById('city').value == "") {
			isValid = false;
			message += "<p>Please enter State and City or Zip code</p>";
		}

		var zipRegex = /^([0-9]{5}$)/;
		var postalRegex = /^[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}\s?[0-9]{1}[a-zA-Z]{1}[0-9]{1}$/;
		var postalNoSpaceRegex = /^[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}$/;
		if (jQuery('#postalCode').val() != '' && !(zipRegex.test(jQuery('#postalCode').val()) || postalRegex.test(jQuery('#postalCode').val()))) {
			isValid = false;
			message += "<p>Please enter a valid Zip or Postal Code</p>";
		}
		//If no space in postal code, add it to avoid bad search
		else if(jQuery('#postalCode').val() != '' && postalNoSpaceRegex.test(jQuery('#postalCode').val()) && isValid) {
			var spaced = jQuery('#postalCode').val().substring(0,3) + " " + jQuery('#postalCode').val().substring(3,6);
			jQuery('#postalCode').val(spaced);
		}
		
		message += "</div>";

		if(!isValid)
		{
			jQuery('#errors').html(message);
		}
		else
		{
			jQuery('#errors').html("");
		}

		return isValid;
	}

	function validateBorderSearch()
	{
		var message = "<div id=\"message\" class=\"error\">";
		var isValid = true;
		
		if (document.getElementById('borderProvinceId').value == "0")
		{
			isValid = false;
			message += "<p>Please enter a Province to continue</p>";
		}
						
		message += "</div>";

		if(!isValid)
		{
			jQuery('#errors').html(message);
		}
		else
		{
			jQuery('#errors').html("");
		}

		return isValid;
	}
	</script>

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<div id="content">

			<div id="findDeliveryLocation">
				<h1>Find an Alternate Delivery Location</h1>
				<br></br>
			</div>	
			<div id="basicInstructions">
				<p>
					Canadians, looking for a US Shipping Address along the US Border? <a id="borderLink" class="borderLinkColor" href="#">Click Here</a>
					<img src="resource/images/overlap-flags.jpg" />					
				</p>
				<br /><br /><br /><br />
			</div>

			<div id="borderInstructions">
				<h1>Find a US Shipping Address</h1>
			</div>
	
			<s:form name="findDepots" id="findDepots" action="/DepotSearch.action">
				<div id="errors"></div>
				<s:errors />
				<s:messages />
				
				<s:hidden name="isBorderSearch" id="isBorderSearch" />
				
				<div id="basicSearch">
					<div id="leftForm">
						<div class="searchTitle">
							Search by State / Province and City
						</div>
						<table class="superplain form">
							<tr>
								<td class="labelcol">
									<s:label class="depotSearchLabel" for="province">State / Province</s:label>
								</td>
								<td class="valcol">
									<s:select id="province" name="province">
										<option value="">Select State/Province</option>
										<option value="">- United States -</option>
										<s:options-collection collection="${actionBean.states}" label="name" value="name" />
										<option value="">- Canada -</option>
										<s:options-collection collection="${actionBean.provinces}" label="name" value="name" />
									</s:select>
								</td>	
							</tr>
							<tr>
								<td>
									<s:label class="depotSearchLabel" for="city">City</s:label>
								</td>
								<td>
									<s:text id="city" name="city"></s:text>
									<small class="validation"></small>
								</td>	
							</tr>
							<tr>
								<td>
									<s:label class="depotSearchLabel" for="cityRadius">Within</s:label>
								</td>
								<td>
									<s:select id="cityRadius" name="cityRadius">
										<option value="10">10 miles</option>
										<option value="50">50 miles</option>
										<option value="100">100 miles</option>
										<option value="200">200 miles</option>
									</s:select>
								</td>	
							</tr>
						</table>
					</div>
					
					<div id="orWrapper">
						<span>OR</span>
					</div>
					
					<div id="rightForm">
						<div class="searchTitle">
							Search by Zip / Postal Code
						</div>
						<table class="superplain form">
							<tr>
								<td class="labelcol">
									<s:label class="depotSearchLabel" for="postalCode">Zip / Postal Code</s:label>
								</td>		
								<td class="valcol">
									<s:text id="postalCode" name="postalCode"></s:text>
									<small class="validation"></small>
								</td>	
							</tr>
							<tr>
								<td>
									<s:label class="depotSearchLabel" for="postalRadius">Within</s:label>
								</td>
								<td>
									<s:select id="postalRadius" name="postalRadius">
										<option value="10">10 miles</option>
										<option value="50">50 miles</option>
										<option value="100">100 miles</option>
										<option value="200">200 miles</option>
									</s:select>
								</td>	
							</tr>
						</table>
					</div>
					<div class="clearFix"></div>
					
					<div class="centerWrapper">
						<s:submit name="search" id="search" value="Search" class="button" onclick="return validateSearch();"/>
					</div>
				</div>
				
				<table id="borderSearch" class="superplain form">
					<tr>
						<td colspan="2">
							<p>
								Return to the <a id="normalLink" href="#">full search</a>.
							</p>
						</td>										
					</tr>
					<tr>
						<td>
							<img id="saveMoney" src="resource/images/kpsearch_savemoney_rightside.jpg" alt="kpsearch_savemoney" />
						</td>
						 <td>
							<table class="superplain form">
								<tr>	
									<td>
										<s:label class="depotSearchBorderLabel" for="borderProvince">Province</s:label>
									</td>
									<td>
										<s:select id="borderProvince" name="borderProvince">
											<option value="0">Select your Province</option>
											<s:options-collection collection="${actionBean.borderProvinces}" label="name" value="stateId" />
										</s:select>
										<s:hidden id="borderProvinceId" name="borderProvinceId" value="0"></s:hidden>
									</td>														
								</tr>
								<tr class="tallrow">
									<td></td>
									<td><s:submit name="search" id="borderSearchBT" value="Search" class="button" onclick="return validateBorderSearch();"/></td>
								</tr>
							</table>
						</td>											
					</tr>								
				</table>
			</s:form>
			
		</div>

	</div>
	<!-- /CONTENTWRAPPER -->
	</s:layout-component>
</s:layout-render>