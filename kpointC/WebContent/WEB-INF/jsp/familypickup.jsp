<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		<div class="clearfix"></div>
		<div id="adbModule">	
			<!-- Family Pickup -->
			<s:errors />
			<s:messages />
			
				<s:form name="familypickupfrm" action="/FamilyPickup.action">
				<table id="familyPickup" width="100%" class="">
					<thead>
						<tr>
							<th></th>
							<th>Name</th>
							<th>Last Name</th>
							<th>Address</th>
							<th></th>
						</tr>						
					</thead>
					<tbody>
						<tr>
							<td>
								<input style="border:none" type="checkbox" name="deleteFamilyPickUp" id="deleteFamilyPickUp">
							</td>
							<td>
									<a href="EditSurrogate.action?surrogateId=1">									
										First Name Last Name
									</a> 
							</td>
							<td>last Name</td>
							<td>
								Address 1
							</td>
							<td>
								<a href="#">display picture</a>
							</td>
						</tr>						
					</tbody>
				</table>
				<div>
					<s:submit class="button" name="addSurrogate" id="addSurrogate" value="Add Surrogate"></s:submit>
					<s:submit class="button" name="deleteSurrogate" id="editSurrogate" value="Delete Surrogate"></s:submit>				
				</div>
				</s:form>
			<!-- //Family Pickup -->	
		</div>	
	</s:layout-component>
</s:layout-render>