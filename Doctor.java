public class Doctor implements Role{
	//tempStat starts out as null, is set to Powerful when a person is healed
	private DefenseStat tempStat;

	public String getRoleName(){
		return "Doctor";
	}

	public int getPriority(){
		return 3;
	}

	public AttackStat getAttackStat(){
		return AttackStat.NONE;
	}

	public DefenseStat getDefenseStat(){
		if (tempStat != null) return tempStat;
  		return DefenseStat.NONE;
	}

	public boolean execute(Player actor, Player target){
		target.getRole().setDefenseStat(DefenseStat.POWERFUL);
		return true;
	}

	public boolean hasRBImunnity(){
		return false;
	}

	public boolean hasControlImmunity(){
		return false;
	}

	public void setDefenseStat(DefenseStat newStat){
		tempStat = newStat;
	}
}