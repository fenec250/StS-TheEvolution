package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import evolutionmod.patches.EvolutionEnum;

public class SpiderBite2
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:SpiderBite";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SpiderBite.png";
    private static final int COST = 3;
    private static final int DAMAGE_AMT = 20;
//    private static final int UPGRADE_DAMAGE_AMT = 1;
    private static final int SPIDER_DAMAGE_AMT = 2;
    private static final int DRONES_AMT = 2;
	private static final int UPGRADE_DRONES_AMT = 1;

    public SpiderBite2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
		this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		if (m != null) {
			if (Settings.FAST_MODE) {
				this.addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.1F));
			} else {
				this.addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.3F));
			}
		}
		AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SMASH));
	}

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT;
		this.baseDamage += SPIDER_DAMAGE_AMT * AbstractDungeon.player.exhaustPile.group.stream()
				.filter(card -> Drone.ID.equals(card.cardID))
				.count();
		supercall.run();
		this.baseDamage = DAMAGE_AMT;
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public void triggerWhenDrawn() {
		for (int i = 0; i < this.magicNumber; ++i) {
			this.addToTop(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(AbstractDungeon.player)));
		}
		super.triggerWhenDrawn();
	}

	@Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_DRONES_AMT);
        }
    }
}