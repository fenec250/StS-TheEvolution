package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class FlameStrike
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:FlameStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 10;
    private static final int UPGRADE_DAMAGE_AMT = 4;

    public FlameStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new LavafolkGene().getChannelAction());
    }

//    @Override
//    public void applyPowers() {
//        calculateBaseDamage();
//        super.applyPowers();
//		this.isDamageModified = this.damage != DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//    }
//
//    @Override
//    public void calculateCardDamage(AbstractMonster mo) {
//        calculateBaseDamage();
//		super.calculateCardDamage(mo);
//		this.isDamageModified = this.damage != DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//    }
//
//    private void calculateBaseDamage() {
//		this.baseDamage = DAMAGE_AMT;
//		if (this.upgraded || BaseEvoCard.isPlayerInThisForm(LavafolkGene.ID)) {
//            this.baseDamage += UPGRADE_DAMAGE_AMT;
//        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameStrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}
