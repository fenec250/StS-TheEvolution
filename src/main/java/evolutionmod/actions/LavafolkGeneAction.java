package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LavafolkGeneAction extends AbstractHalfTargetedAction {

	private int damage;

	public LavafolkGeneAction(AbstractPlayer player, AbstractMonster monster, int damage) {
		super(player, monster);
		this.source = player;
		this.damage = damage;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DAMAGE;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		AbstractDungeon.actionManager.addToTop(new DamageAction(
				this.target, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS), AttackEffect.NONE, true));

		this.isDone = true;

		this.tickDuration();
	}
}
