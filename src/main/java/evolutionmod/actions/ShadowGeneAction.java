package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.List;
import java.util.stream.Collectors;

public class ShadowGeneAction extends AbstractHalfTargetedAction {


	public ShadowGeneAction(AbstractPlayer player, AbstractMonster monster) {
		super(player, monster);
		this.source = player;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = (this.target == null || this.target.isDeadOrEscaped()) ? ActionType.DEBUFF
				: monster.hasPower(WeakPower.POWER_ID) ? ActionType.DAMAGE : ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		List<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters
				.stream().filter(m -> !m.isDeadOrEscaped() && m.hasPower(WeakPower.POWER_ID))
				.collect(Collectors.toList());
		int damage = GameActionManager.turn + monsters.size();
		AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(
				(AbstractCreature)null,
				DamageInfo.createDamageMatrix(damage, true),
				DamageInfo.DamageType.THORNS,
				AbstractGameAction.AttackEffect.FIRE));

		this.isDone = true;
		this.tickDuration();
	}
	public boolean updateTarget() {
		boolean s = super.updateTarget();
		this.actionType = this.target.hasPower(WeakPower.POWER_ID)?ActionType.DAMAGE:ActionType.DEBUFF;
		return s;
	}
}
