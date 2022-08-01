package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class SeaWolf2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:SeaWolf";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeaWolf.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 0;
    private static final int MERFOLK_BLOCK_AMT = 3;
    private static final int UPGRADE_MERFOLK_BLOCK_AMT = 1;

    public SeaWolf2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = MERFOLK_BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isPlayerInThisForm(MerfolkGene.ID)) {
            addToBot(new GainBlockAction(p, this.block));
        }
        addToBot(new DamageAction(
						m, new DamageInfo(p, damage, damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        formEffect(MerfolkGene.ID);
    }

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
		rawDescription = upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0];
		initializeDescription();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = (AbstractDungeon.player.currentBlock + (isPlayerInThisForm(MerfolkGene.ID) ? this.block : 0)) / (upgraded ? 1 : 2);
		supercall.run();
		this.baseDamage = 0;
		this.isDamageModified = this.damage != this.baseDamage;
	}

    @Override
    public AbstractCard makeCopy() {
        return new SeaWolf2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeBlock(UPGRADE_MERFOLK_BLOCK_AMT);
			this.rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(MerfolkGene.ID)) {
			this.glowColor = MerfolkGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}