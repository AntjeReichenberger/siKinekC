<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-kpsearch.css" type="text/css" />
	
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('#normalLink').click(function(e) {
				e.preventDefault();
				hideBorderSearch();
			});

			jQuery('#backLink').click(function(e) {
				e.preventDefault();
				hideLocalKPSearch();
			});

			jQuery('#borderProvince').change(function() {
				jQuery('#borderProvinceId').val(jQuery('#borderProvince').val());
			});

			if (jQuery('#selectedCountryName').val() == "United States") {
				showDefaultSearch();
				hideCanadianSearch();
			}
			else {
				if (jQuery('#returnFromResults').val() == "true") {
					if (jQuery('#borderProvinceId').val() > 0) {
						showBorderSearch();
					}
					else {
						showLocalKPSearch();
					}
				}
				else {
					showCanadianSearch();
					hideDefaultSearch();
				}
			}
			
		});

		function showLocalKPSearch() {
			showDefaultSearch();
			jQuery('#back').show();
			hideCanadianSearch();
		}

		function hideLocalKPSearch() {
			hideDefaultSearch();
			showCanadianSearch();
		}

		function showDefaultSearch() {
			jQuery('#findDeliveryLocation').show();
			jQuery('#basicSearch').show();
		}

		function hideDefaultSearch() {
			jQuery('#findDeliveryLocation').hide();
			jQuery('#basicSearch').hide();
		}

		function showCanadianSearch() {
			jQuery('#canadianSearch').show();
		}

		function hideCanadianSearch() {
			jQuery('#canadianSearch').hide();
		}

		function showBorderSearch() {
			jQuery('#borderSearch').show();
			hideCanadianSearch();
		}

		function hideBorderSearch() {
			jQuery('#borderSearch').hide();
			showCanadianSearch();
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
	
	<br class="clear" />

	<div id="adbModule" >	
		<!-- CONTENT -->
		<div>			
			<div id="findDeliveryLocation">
				<h1>Find a KinekPoint</h1>
				<br></br>
			</div>
			
			<div id="borderInstructions">
				<h1>Find a US Shipping Address</h1>
			</div>
	
			<s:form name="findDepots" id="findDepots" action="/ChooseDefaultKinekPoint.action">
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
						<span id="back"><a id="backLink" href="#">Back</a>|</span>
						<s:submit name="search" id="search" value="Search" class="button" onclick="return validateSearch();"/>
					</div>
				</div>
					
				<div id="canadianSearch">
					<div id="typeOfKP">
						<span>Please choose the type of KinekPoint you are looking for:</span>
					</div>
				
					<div id="leftForm">
						<s:submit name="usBorderSearch" id="usBorderSearch" value="US Border KinekPoint" class="button" onclick="showBorderSearch(); return false;"/>
					</div>
					
					<div id="orWrapper">
						<span>OR</span>
					</div>
					
					<div id="rightForm">
						<s:submit name="localKinekPoint" id="localKinekPoint" value="Local KinekPoint" class="button" onclick="showLocalKPSearch(); return false;"/>
					</div>
				</div>
				
				<table id="borderSearch" class="superplain form">
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
									<td id="borderSubmit" colspan="2">
										<a id="normalLink" href="#">Back</a>
										|
										<s:submit name="search" id="borderSearchBT" value="Search" class="button" onclick="return validateBorderSearch();"/>
									</td>
								</tr>
							</table>
						</td>											
					</tr>								
				</table>			
			
				<s:hidden name="selectedCountryName" id="selectedCountryName"></s:hidden>
				<s:hidden name="returnFromResults" id="returnFromResults" />
			</s:form>
			<div class="clearFix"></div>
		</div>
		<!-- /CONTENT -->
	</div>
	<!-- /CONTENTWRAPPER -->
</s:layout-component>
</s:layout-render>
