package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.SeduceAction;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class Seduce
        extends CustomCard {
    public static final String ID = "evolutionmod:Seduce";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 9;
    private static final int UPGRADE_DAMAGE_AMT = 4;

    public Seduce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        HeavyBlade;
        AbstractDungeon.actionManager.addToBottom(new SeduceAction(
                p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new SuccubusGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Seduce();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}