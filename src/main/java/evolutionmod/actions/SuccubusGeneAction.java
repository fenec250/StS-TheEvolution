package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class SuccubusGeneAction extends AbstractHalfTargetedAction {

	private int vulnerable;

	public SuccubusGeneAction(AbstractPlayer player, AbstractMonster monster, int vulnerable) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.vulnerable = vulnerable;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, this.vulnerable, false), this.vulnerable));

		this.isDone = true;

		this.tickDuration();
	}
}
